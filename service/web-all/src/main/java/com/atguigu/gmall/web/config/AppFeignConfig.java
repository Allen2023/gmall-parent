package com.atguigu.gmall.web.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 22:16
 */
@EnableFeignClients(basePackages = "com.atguigu.gmall.web.feign")
@Configuration
public class AppFeignConfig {

}
