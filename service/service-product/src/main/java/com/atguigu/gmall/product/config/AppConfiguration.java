package com.atguigu.gmall.product.config;

import com.atguigu.gmall.common.config.MybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 20:40
 */
@MapperScan(basePackages = "com.atguigu.gmall.product.mapper")
@Import(MybatisPlusConfig.class)
@Configuration
public class AppConfiguration {
}
