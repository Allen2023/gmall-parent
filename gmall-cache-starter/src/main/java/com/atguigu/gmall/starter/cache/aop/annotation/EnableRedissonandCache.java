package com.atguigu.gmall.starter.cache.aop.annotation;


import com.atguigu.gmall.starter.cache.impl.CatchServiceImpl;
import com.atguigu.gmall.starter.redisson.RedissonAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import({RedissonAutoConfiguration.class, CatchServiceImpl.class})
public @interface EnableRedissonandCache {
}
