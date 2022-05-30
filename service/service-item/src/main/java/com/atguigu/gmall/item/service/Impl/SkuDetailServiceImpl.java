package com.atguigu.gmall.item.service.Impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.list.SearchFeignClient;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.starter.cache.CatchService;
import com.atguigu.gmall.starter.cache.aop.annotation.Cache;
import com.atguigu.gmall.starter.constants.RedisConst;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 23:32
 */
@Slf4j
@Service
public class SkuDetailServiceImpl implements SkuDetailService {
    @Autowired
    ProductFeignClient productFeignClient;

    @Autowired
    ThreadPoolExecutor corePool;


    @Autowired
    CatchService catchService;

    @Autowired
    RBloomFilter<Object> skuIdBloom;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    SearchFeignClient searchFeignClient;


    //商品详情服务：
    //查询sku详情得做这么多式
    //1、查分类
    //2、查Sku信息
    //3、查sku的图片列表
    //4、查价格
    //5、查所有销售属性组合
    //6、查实际sku组合
    //7、查介绍(不用管)
    @Cache(cacheKey = RedisConst.SKU_CACHE_KEY_PREFIX + "#{#args[0]}",
            bloomName = "skuIdBloom", bloomValue = "#{#args[0]}")
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) throws InterruptedException {
        log.info("从数据库查询商品详情" + skuId + "信息");
        return getSkuDetialFromDb(skuId);
    }


    /**
     * 用Redisson查询
     *
     * @param skuId
     * @return
     */
    //@Override
    public SkuDetailTo getSkuDetailByRedissonLock(Long skuId) throws InterruptedException {
        String key = RedisConst.SKU_CACHE_KEY_PREFIX + skuId;
        log.info("从缓存中读取数据");
        //1.从缓存中读数据
        SkuDetailTo cacheData = catchService.getCacheData(key, new TypeReference<SkuDetailTo>() {
        });
        log.info("缓存未命中");
        if (cacheData == null) {
            log.info("缓存中没有数据,从redis布隆过滤器查询是否有此skuId");
            //2.缓存中没有数据,回源数据库查询
            if (skuIdBloom.contains(skuId)) {
                log.info("布隆过滤器中有该id 回源数据库查询");
                //3.布隆过滤器中有该id 则回源数据库
                RLock lock = redissonClient.getLock(RedisConst.SKUDETAIL_LOCK_PREFIX + skuId);
                boolean tryLock = false;
                try {
                    tryLock = lock.tryLock();
                    if (tryLock) {
                        log.info("抢到锁,开始查询数据库");
                        //4.如果抢到锁,回源数据库查询
                        SkuDetailTo skuDetailFromDb = getSkuDetialFromDb(skuId);
                        log.info("查询到该数据,存入缓存");
                        catchService.saveCatchData(key, skuDetailFromDb);
                        return skuDetailFromDb;
                    }
                } finally {
                    try {//5.解锁
                        log.info("查询结束,解锁");
                        if (tryLock) lock.unlock();
                    } catch (Exception e) {
                        log.error("SkuDetail：又想解别人锁了.... {}", e);
                    }
                }
                //6.抢锁失败
                log.info("抢锁失败,等待1s查询缓存数据" + skuId);
                try {
                    Thread.sleep(1000);
                    cacheData = catchService.getCacheData(key, new TypeReference<SkuDetailTo>() {
                    });
                    log.info("查询到缓存数据,返回结果");
                    return cacheData;
                } catch (InterruptedException e) {
                    log.error("SkuDetail：睡眠异常：{}", e);
                }
            }
            //数据库中无id 布隆说没有
            log.info("skuId缓存未命中,数据库中无此id,拦截", skuId);
            return null;
        }
        //缓存中有数据
        log.info("缓存命中" + skuId);
        //缓存命中率？越高越好
        // 1-1: 0
        // 2-2： 命中/总请求 = 0.5
        // 3-3:  2/3 = 0.6667
        // N-N;  (n-1)/n = 0.99999999
        return cacheData;
    }

    /**
     * 从数据库查SKuDetail
     *
     * @param skuId
     * @return
     */
    public SkuDetailTo getSkuDetialFromDb(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();
        //异步 编排: 编组(管理)+排列组合(运行)
        CompletableFuture<Void> categoryTask = CompletableFuture.runAsync(() -> {
            //1、查分类
            Result<BaseCategoryView> skuCategoryView = productFeignClient.getSkuCategoryView(skuId);
            if (skuCategoryView.isOk()) {
                skuDetailTo.setCategoryView(skuCategoryView.getData());
            }
        }, corePool);

        CompletableFuture<Void> SkuTask = CompletableFuture.runAsync(() -> {
            //2、查Sku信息 照片
            Result<SkuInfo> skuInfo = productFeignClient.getSkuInfo(skuId);
            if (skuInfo.isOk()) {
                skuDetailTo.setSkuInfo(skuInfo.getData());
            }
        }, corePool);

        CompletableFuture<Void> PriceTask = CompletableFuture.runAsync(() -> {
            //3.查询sku价格
            Result<BigDecimal> skuPrice = productFeignClient.getSkuPrice(skuId);
            if (skuPrice.isOk()) {
                skuDetailTo.setPrice(skuPrice.getData());
            }
        }, corePool);
        CompletableFuture<Void> SpuSaleAttrTask = CompletableFuture.runAsync(() -> {
            //4、查所有销售属性组合
            Result<List<SpuSaleAttr>> skudeSpuSaleAttrAndValue = productFeignClient.getSkudeSpuSaleAttrAndValue(skuId);
            if (skudeSpuSaleAttrAndValue.isOk()) {
                skuDetailTo.setSpuSaleAttrList(skudeSpuSaleAttrAndValue.getData());
            }
        }, corePool);
        CompletableFuture<Void> valueJsonTask = CompletableFuture.runAsync(() -> {
            //5、查实际sku组合
            Result<Map<String, String>> skuValueJson = productFeignClient.getSkuValueJson(skuId);
            if (skuValueJson.isOk()) {
                Map<String, String> jsonData = skuValueJson.getData();
                skuDetailTo.setValuesSkuJson(JSONs.toStr(jsonData));
            }
        }, corePool);

        //allOf 返回的 CompletableFuture 总任务结束再往下
        CompletableFuture.allOf(categoryTask, SkuTask, PriceTask, SpuSaleAttrTask, valueJsonTask).join();
        return skuDetailTo;
    }

    /**
     * 增加商品热度
     *
     * @param skuId
     */
    @Override
    public void updateHotScore(Long skuId) {
        //累积到100人以后增加一次热度
        Double score = stringRedisTemplate.opsForZSet().incrementScore(RedisConst.SKU_HOTSCORE, skuId.toString(), 1.0);
        if (score % 100 == 0) {
            //远程调用ES增加商品热度
            searchFeignClient.updateHotScore(skuId, score.longValue());
        }

    }

    /**
     * 缓存查询SKuDetail
     *
     * @param skuId
     * @return
     */
    //@Override
    public SkuDetailTo getSkuDetailFromRedis(Long skuId) {
        String key = RedisConst.SKU_CACHE_KEY_PREFIX + skuId;
        //1.从缓存中查数据
        SkuDetailTo cacheData = catchService.getCacheData(key, new TypeReference<SkuDetailTo>() {
        });
        if (cacheData == null) {
            //2.缓存中没有数据,查库[回源]
            //回源之前,先经过布隆过滤器 如果布隆过滤器中有 则回源 反之则不回
            if (skuIdBloom.contains(skuId)) {
                //数据库中有此id从数据库查数据
                // log.info("SkuDetial缓存未命中,回源数据", skuId);
                String token = UUID.randomUUID().toString();//随机的UUID值保证不因为服务器宕机删除其他线程的锁
                Boolean lock = stringRedisTemplate.opsForValue().//加锁 /设置过期时间 10秒 即使断电也可以删除锁避免死锁
                        setIfAbsent(RedisConst.LOCK_PREFIX + skuId, token, 10, TimeUnit.SECONDS);
                SkuDetailTo detailFromDb = null;
                if (lock) {//抢锁成功
                    try {
                        log.info("缓存中无数据,查询数据库");
                        //在数据库查询之前记得加锁
                        detailFromDb = getSkuDetialFromDb(skuId);
                        //保存在缓存中
                        log.info("回源数据保存进缓存");
                        catchService.saveCatchData(key, detailFromDb);
                    } finally {
                        //释放锁 删除redis中的lock
                        log.info("分布式锁抢锁失败,1s后直接查缓存");
                        //lua脚本原子执行 如果值相等则删除锁反之删除失败
                        String deletescript =
                                "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                        Long result = stringRedisTemplate.execute(new DefaultRedisScript<>(deletescript, Long.class),
                                Arrays.asList(RedisConst.LOCK_PREFIX + skuId), token);

                        if (result.longValue() == 1L) {
                            log.info("是自己的锁,删除锁完成");
                        } else {
                            log.info("这是别人的锁,不能删");
                        }
                        /*String uuID = stringRedisTemplate.opsForValue().get("lock");
                        if (uuID.equals(token)) {
                            stringRedisTemplate.delete("lock");
                            log.info("是自己的锁,删除锁完成");
                        } else {
                            log.info("这是别人的锁,不能删");
                        }*/
                    }
                } else {//抢锁失败
                    try {
                        Thread.sleep(1000);//睡一秒再查缓存
                        cacheData = catchService.getCacheData(key, new TypeReference<SkuDetailTo>() {
                        });
                        return cacheData;
                    } catch (InterruptedException e) {

                    }
                }
                return detailFromDb;
            }
            //数据库中无id 布隆说没有
            log.info("skuId缓存未命中,数据库中无此id,拦截", skuId);
            return null;
        }
        log.info("缓存命中");
        //缓存中有数据
        return cacheData;
    }


}
