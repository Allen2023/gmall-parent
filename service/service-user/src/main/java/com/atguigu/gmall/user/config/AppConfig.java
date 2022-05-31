package com.atguigu.gmall.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/31 18:59
 */
@MapperScan(basePackages = "com.atguigu.gmall.user.mapper")
@EnableTransactionManagement
@Configuration
public class AppConfig {
}
