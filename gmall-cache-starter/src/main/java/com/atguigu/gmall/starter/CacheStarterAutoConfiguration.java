package com.atguigu.gmall.starter;


import com.atguigu.gmall.starter.cache.aop.annotation.EnableAutoCache;
import com.atguigu.gmall.starter.cache.aop.annotation.EnableRedissonandCache;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/26 23:31
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)//先配置好Redis
@Configuration
@EnableAutoCache//开启自动缓存
@EnableRedissonandCache//开启Redisson 布隆过滤器
public class CacheStarterAutoConfiguration {
}
