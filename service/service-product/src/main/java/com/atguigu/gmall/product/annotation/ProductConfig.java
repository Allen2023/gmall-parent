package com.atguigu.gmall.product.annotation;


import org.mybatis.spring.annotation.MapperScan;
import java.lang.annotation.*;

/**
 * 商品服务配置注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
@Inherited
//@ComponentScan(basePackages = "com.atguigu.gmall.common.config")
public @interface ProductConfig {
}
