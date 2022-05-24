package com.atguigu.gmall.product;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/24 16:15
 */
//@SpringBootTest
public class RedisTest {

    /* @Autowired
     StringRedisTemplate stringRedisTemplate;


     //@Test
     void redisTest(){
         stringRedisTemplate.opsForValue().set("hello", "world");
         String hello = stringRedisTemplate.opsForValue().get("hello");
         System.out.println("hello = " + hello);

     }*/
    @Autowired
    RedissonClient redissonClient;

    //@Test
    void redissonTest() {
        //1.获取布隆过滤器
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("sku:bloom");
        //2.初始化布隆
        // long expectedInsertions  预期的插入值
        //double falseProbability  错误概率
        bloomFilter.tryInit(1000000, 0.00001);

        //保存数据
        bloomFilter.add(11L);
        bloomFilter.add(12L);
        bloomFilter.add(13L);

        //判断有无数据
        System.out.println("bloomFilter.contains(11L) = " + bloomFilter.contains(11L));
        System.out.println("bloomFilter.contains(12L) = " + bloomFilter.contains(12L));
        System.out.println("bloomFilter.contains(13L) = " + bloomFilter.contains(13L));


    }
    //@Test
    void aaa(){
        System.out.println("redissonClient.getBloomFilter(\"sku:bloom\") = " + redissonClient.getBloomFilter("sku:bloom"));

    }

}
