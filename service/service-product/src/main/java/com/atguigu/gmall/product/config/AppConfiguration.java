package com.atguigu.gmall.product.config;

import com.atguigu.gmall.common.annotation.EnableMinio;
import com.atguigu.gmall.common.annotation.EnableSwagger;
import com.atguigu.gmall.common.config.MybatisPlusConfig;
import com.atguigu.gmall.product.annotation.ProductConfig;
import io.minio.MinioClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 配置类
 *
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 20:40
 */
@EnableSwagger
@ProductConfig
@EnableMinio//开启Minio
@Configuration
public class AppConfiguration {


}
