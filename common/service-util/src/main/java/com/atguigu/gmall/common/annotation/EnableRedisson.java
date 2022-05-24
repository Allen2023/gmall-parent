package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.redisson.RedissonAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(RedissonAutoConfiguration.class)
public @interface EnableRedisson {
}
