package com.atguigu.gmall.product.config;

import com.atguigu.gmall.common.annotation.*;
import com.atguigu.gmall.product.annotation.ProductConfig;
import com.atguigu.gmall.starter.cache.aop.annotation.EnableAutoCache;
import com.atguigu.gmall.starter.cache.aop.annotation.EnableRedissonandCache;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 配置类
 *
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 20:40
 */
@EnableAutoCache//开启自动缓存
@EnableRedissonandCache//开启Redisson 布隆过滤器
@EnableAutoHandleException //开启全局异常处理
@EnableMybatisPlusConfig //开启Mybatis-plus分页插件
@EnableSwaggerApi //开启Swagger
@ProductConfig //开启mapper扫描
@EnableMinio//开启Minio
@EnableScheduling
@Configuration
public class AppConfiguration {



}
