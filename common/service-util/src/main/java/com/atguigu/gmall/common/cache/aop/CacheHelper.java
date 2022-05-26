package com.atguigu.gmall.common.cache.aop;

import com.atguigu.gmall.common.cache.CatchService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/26 15:14
 */
@Component
public class CacheHelper {
    @Autowired
    CatchService catchService;

    @Qualifier("skuIdBloom")
    @Autowired
    RBloomFilter<Object> skuIdBloom;

    @Autowired
    RedissonClient redissonClient;


    /**
     * 获取缓存数据
     *
     * @param cacheKey
     * @param joinPoint
     * @return
     */
    public Object getCacheData(String cacheKey, ProceedingJoinPoint joinPoint) {
        //获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取方法名
        Method method = signature.getMethod();
        //获取方法值泛型返回值类型
        Type genericReturnType = method.getGenericReturnType();
        Object cacheData = catchService.getCacheData(cacheKey, new TypeReference<Object>() {
            @Override
            public Type getType() {
                return genericReturnType;
            }
        });
        return cacheData;
    }

    /**
     * 保存缓存数据
     *
     * @param cacheKey
     * @param result
     */
    public void saveCatchData(String cacheKey, Object result) {
        catchService.saveCatchData(cacheKey, result);
    }

    /**
     * 布隆过滤器
     *
     * @param arg
     * @return
     */
    public boolean bloomTest(Object arg) {
        boolean contains = skuIdBloom.contains(arg);
        return contains;
    }

    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    public boolean tryLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean tryLock = lock.tryLock();
        return tryLock;
    }

    /**
     * 解锁
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (lock.isLocked()) {
                lock.unlock();
            }
        } catch (Exception e) {

        }
    }
}
