package com.atguigu.gmall.item.service.Impl;

import com.atguigu.gmall.common.cache.CatchService;
import com.atguigu.gmall.common.constants.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

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
    RBloomFilter<Object> skuBloom;
    //商品详情服务：
    //查询sku详情得做这么多式
    //1、查分类
    //2、查Sku信息
    //3、查sku的图片列表
    //4、查价格
    //5、查所有销售属性组合
    //6、查实际sku组合
    //7、查介绍(不用管)

    /**
     * 缓存查询
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailTo getSkuDetial(Long skuId) {
        String key = RedisConst.SKU_CACHE_KEY_PREFIX + skuId;
        //1.从缓存中查数据
        SkuDetailTo cacheData = catchService.getCacheData(key, new TypeReference<SkuDetailTo>() {
        });
        if (cacheData == null) {
            //2.缓存中没有数据,查库[回源]
            //回源之前,先经过布隆过滤器 如果布隆过滤器中有 则回源 反之则不回
            if (skuBloom.contains(skuId)) {
                //数据库中有此id从数据库查数据
                log.info("SkuDetial缓存未命中,回源数据", skuId);
                SkuDetailTo detialFromDb = getSkuDetialFromDb(skuId);
                //保存在缓存中
                log.info("回源数据保存进缓存");
                catchService.saveCatchData(key, detialFromDb);
                return detialFromDb;
            }
            //数据库中无id 布隆说没有
            log.info("skuId缓存未命中,数据库中无此id,拦截", skuId);
            return null;
        }
        log.info("缓存命中");
        //缓存中有数据
        return cacheData;
    }

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


}
