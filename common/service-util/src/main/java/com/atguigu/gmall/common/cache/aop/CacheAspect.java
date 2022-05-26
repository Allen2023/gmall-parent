package com.atguigu.gmall.common.cache.aop;

import com.atguigu.gmall.common.constants.RedisConst;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/26 14:21
 */
@Component
@Aspect
public class CacheAspect {

    @Autowired
    CacheHelper cacheHelper;



    @Around(value = "@annotation(com.atguigu.gmall.common.cache.aop.annotation.Cache)")
    public Object around(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();//目标方法参数
        String cacheKey = RedisConst.SKU_CACHE_KEY_PREFIX + args[0];
        Object result = null;
        try {
            //前置通知  先查询缓存 中有没有这个数据
            Object cacheData = cacheHelper.getCacheData(cacheKey,joinPoint);
            if (cacheData == null) {
                boolean contains = cacheHelper.bloomTest(args[0]);//布隆过滤器验证
                if (contains) {//布隆过滤器验证成功
                    String lockKey = RedisConst.SKUDETAIL_LOCK_PREFIX + args[0];
                    boolean tryLock = cacheHelper.tryLock(lockKey);
                        if (tryLock) {
                            result = joinPoint.proceed();//执行目标方法 查询数据库
                            cacheHelper.saveCatchData(cacheKey, result);
                            //解锁
                            cacheHelper.unlock(lockKey);
                            return result;
                        }
                    //没加锁成功
                    Thread.sleep(1000);
                    cacheData = cacheHelper.getCacheData(cacheKey, joinPoint);
                    return cacheData;
                } else {//布隆过滤器验证失败
                    return null;
                }
            }
            //缓存中有数据
            return cacheData;
            //返回通知
        } catch (Throwable e) {
            //异常通知
        } finally {
            //后置通知
        }
        return result;//目标方法返回结果
    }
}
