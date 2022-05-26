package com.atguigu.gmall.item.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/26 10:24
 */

@RestController
public class RedissonLockTest {
    @Autowired
    RedissonClient redissonClient;

    /**
     * lock.unlock
     * 1.特性 锁默认时间30s 防止服务宕机导致的死锁
     * 2.锁会自动续期 业务超长就会自动续期代码运行到直至解锁
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/lock/hello")
    public String RedissonLockTest() throws InterruptedException {
        RLock lock = redissonClient.getLock("hello-lock");
        boolean tryLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
        if (tryLock) {
            System.out.println("hello");
            Thread.sleep(1000);
            lock.unlock();
            lock.lock();
        }
       /* lock.tryLock(5, 10, TimeUnit.SECONDS);
        lock.lock();//一直等锁
        lock.unlock();*/
        return "ok";
    }
}
