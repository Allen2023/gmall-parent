package com.atguigu.gmall.cart.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 19:02
 */
@EnableFeignClients(basePackages ="com.atguigu.gmall.feign.product" )
@Configuration
public class Appconfig {
}
