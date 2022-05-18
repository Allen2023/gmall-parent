package com.atguigu.gmall.product;

import com.atguigu.gmall.common.config.MybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

import org.springframework.context.annotation.Import;




@SpringCloudApplication
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
