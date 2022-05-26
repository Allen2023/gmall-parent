package com.atguigu.gmall.item.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/26 9:32
 */
@RestController
public class RedissonTest {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate redisTemplate;


    @GetMapping("/redis/incr")
    public String RedissonIncrTest() {
        RLock lock = redissonClient.getLock("lock:A");

        try {
            lock.lock();//加锁
            String hellocount = redisTemplate.opsForValue().get("hellocount");//取值
            int count = Integer.parseInt(hellocount);
            count++;//自增
            redisTemplate.opsForValue().set("hellocount", count + "");//设置值
        } finally {
            try {
                lock.unlock();//解锁 如果不是自己的锁就自动抛出异常
            } catch (Exception e) {}
        }
        return "ok";
    }
}
