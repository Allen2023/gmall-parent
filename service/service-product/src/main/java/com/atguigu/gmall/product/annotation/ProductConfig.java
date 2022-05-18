package com.atguigu.gmall.product.annotation;

import com.atguigu.gmall.common.config.MybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 商品服务配置注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
@Import(MybatisPlusConfig.class)
public @interface ProductConfig {
}
