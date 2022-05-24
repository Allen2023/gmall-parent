package com.atguigu.gmall.product.config;

import com.atguigu.gmall.common.annotation.*;
import com.atguigu.gmall.product.annotation.ProductConfig;
import org.springframework.context.annotation.Configuration;


/**
 * 配置类
 *
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 20:40
 */
@EnableRedisson//开启Redisson 布隆过滤器
@EnableAutoHandleException //开启全局异常处理
@EnableMybatisPlusConfig //开启Mybatis-plus分页插件
@EnableSwaggerApi //开启Swagger
@ProductConfig //开启mapper扫描
@EnableMinio//开启Minio
@Configuration
public class AppConfiguration {


}
