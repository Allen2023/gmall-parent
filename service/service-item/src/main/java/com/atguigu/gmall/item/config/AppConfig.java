package com.atguigu.gmall.item.config;


import com.atguigu.gmall.common.annotation.EnableAppThreadPool;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;


//每个微服务中，自己需要调用谁就导入谁
@EnableAppThreadPool//开启自定义线程池配置
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.product")
@Configuration
public class AppConfig {


}
