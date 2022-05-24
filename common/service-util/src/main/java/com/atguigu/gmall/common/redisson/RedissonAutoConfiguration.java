package com.atguigu.gmall.common.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/24 21:27
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)//必须在redis自动配置之后再进行
@Configuration
public class RedissonAutoConfiguration {
    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        // 默认连接地址 127.0.0.1:6379
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+redisProperties.getHost() + ":" + redisProperties.getPort())
                .setPassword(redisProperties.getPassword());
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
