package com.atguigu.gmall.starter.cache.aop;

import com.atguigu.gmall.starter.cache.CatchService;
import com.atguigu.gmall.starter.cache.aop.annotation.Cache;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/26 15:14
 */
@Slf4j
@Component
public class CacheHelper {
    @Autowired
    CatchService catchService;

    @Qualifier("skuIdBloom")
    @Autowired
    RBloomFilter<Object> skuIdBloom;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    Map<String,RBloomFilter<Object>> bloomMap;

    SpelExpressionParser parser = new SpelExpressionParser();

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
     * 判断指定的布隆过滤器中有没有此id
     *
     * @param bloomName
     * @param joinPoint
     * @return
     */
    public boolean bloomTest(Object bloomName, ProceedingJoinPoint joinPoint) {
        Cache cache = getMethodCacheAnnotation(joinPoint);
        String bloomValue = cache.bloomValue();

        //计算出布隆过滤器需要判定的值
        Object bloomExpValue = getExpressionValue(joinPoint, bloomValue,Object.class);
        log.info("判定布隆过滤器是有此id"+bloomExpValue);
        RBloomFilter<Object> bloomFilter = bloomMap.get(bloomName);
        return  bloomFilter.contains(bloomExpValue);
    }

    /**
     * 解析表达式获取注解上的参数值
     * @param joinPoint
     * @param cacheKeyExpression
     * @return
     */
    private <T> T getExpressionValue(ProceedingJoinPoint joinPoint, String cacheKeyExpression,Class<T> clazz) {
        //解析表达式
        //表达式
        Expression expression = parser.parseExpression(cacheKeyExpression, ParserContext.TEMPLATE_EXPRESSION);
        //准备上下文信息
        StandardEvaluationContext context = new StandardEvaluationContext();
        //将skuId数组放进上下文信息
        context.setVariable("args", joinPoint.getArgs());
        //得到值
        T value = expression.getValue(context, clazz);
        return value;
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

    /**
     * 注解解析器
     *
     * @param joinPoint
     * @return
     */
    public String evaluateExpression(ProceedingJoinPoint joinPoint) {
        //拿到注解
        Cache cache = getMethodCacheAnnotation(joinPoint);
        //拿到表达式 cacheKey = RedisConst.SKU_CACHE_KEY_PREFIX+"#{#args[0]}")
        String cacheKey = StringUtils.isEmpty(cache.cacheKey()) ? cache.value() : cache.cacheKey();
        //解析表达式
        String value = getExpressionValue(joinPoint, cacheKey,String.class);

        return value;
    }

    /**
     * 判断是否需要布隆过滤器
     * @param joinPoint
     * @return
     */
    public String determineBloom(ProceedingJoinPoint joinPoint) {
        //拿到方法注解
        Cache cache = getMethodCacheAnnotation(joinPoint);

        //拿到布隆名
        String bloomName = cache.bloomName();
        //返回布隆名
        return bloomName;
    }

    /**
     * 获取注解信息
     * @param joinPoint
     * @return
     */
    private Cache getMethodCacheAnnotation(ProceedingJoinPoint joinPoint) {
        //拿到方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //拿到方法
        Method method = signature.getMethod();
        //拿到注解
        Cache cache = AnnotationUtils.findAnnotation(method, Cache.class);
        return cache;
    }

}
