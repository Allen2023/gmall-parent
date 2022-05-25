package com.atguigu.gmall.common.redisson;

import com.atguigu.gmall.common.constants.RedisConst;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/24 21:27
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)//必须在redis自动配置之后再进行
@Configuration
public class RedissonAutoConfiguration {

    @Autowired(required = false)
    List<BloomTask> bloomTask;

    /**
     * 配置RedissonClient
     * @param redisProperties
     * @return
     */
    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        // 默认连接地址 127.0.0.1:6379
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setPassword(redisProperties.getPassword());
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    /**
     * 布隆过滤器配置
     * @param redissonClient
     * @return
     */
    @Bean
    public RBloomFilter<Object> skuIdBloom(RedissonClient redissonClient) {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(RedisConst.BLOOM_SKU_ID);
        if (bloomFilter.isExists()) {
            return bloomFilter;//存在布隆直接返回
        } else {
            //如果不存在则初始化
            bloomFilter.tryInit(500000, 0.0000001);
            //准备数据
            for (BloomTask task : bloomTask) {
                if (task instanceof SkuBloomTask){
                    //只运行skuBloom的bloom任务
                    task.initData(bloomFilter);
                }
            }
        }
        return bloomFilter;
    }

 /*   @Bean
    public RBloomFilter<Long> orderBloom(RedissonClient redissonClient) {
        RBloomFilter<Long> bloomFilter = redissonClient.getBloomFilter(RedisConst.BLOOM_SKU_ID);
        if (bloomFilter.isExists()) {
            return bloomFilter;//存在布隆直接返回
        } else {
            //如果不存在则初始化
            bloomFilter.tryInit(500000, 0.0000001);
            //准备数据
            for (BloomTask task : bloomTask) {
                if (task instanceof OrderBloomTask){
                    task.initData();
                }
            }
        }
        return bloomFilter;
    }*/
}
