package com.atguigu.gmall.starter.cache.aop.annotation;


import com.atguigu.gmall.starter.cache.aop.CacheAspect;
import com.atguigu.gmall.starter.cache.aop.CacheHelper;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({CacheAspect.class, CacheHelper.class})
public @interface EnableAutoCache {
}
