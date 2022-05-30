package com.atguigu.gmall.product.config;

import com.atguigu.gmall.common.annotation.*;
import com.atguigu.gmall.product.annotation.ProductConfig;
import com.atguigu.gmall.starter.cache.aop.annotation.EnableAutoCache;
import com.atguigu.gmall.starter.cache.aop.annotation.EnableRedissonandCache;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 配置类
 *
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 20:40
 */

@EnableAutoHandleException //开启全局异常处理
@EnableMybatisPlusConfig //开启Mybatis-plus分页插件
@EnableSwaggerApi //开启Swagger
@ProductConfig //开启mapper扫描
@EnableMinio//开启Minio
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.list")
@EnableScheduling
@Configuration
public class AppConfiguration {



}
