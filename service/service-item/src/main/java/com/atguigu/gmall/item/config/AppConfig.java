package com.atguigu.gmall.item.config;


import com.atguigu.gmall.common.annotation.EnableAppThreadPool;

import com.atguigu.gmall.common.annotation.EnableRedissonandCache;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


//每个微服务中，自己需要调用谁就导入谁
@EnableAspectJAutoProxy//开启切面自动代理
@EnableRedissonandCache//开启Redisson
@EnableAppThreadPool//开启自定义线程池配置
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.product")
@Configuration
public class AppConfig {


}
