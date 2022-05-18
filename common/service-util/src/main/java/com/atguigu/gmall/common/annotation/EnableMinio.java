package com.atguigu.gmall.common.annotation;


import com.atguigu.gmall.common.config.MybatisPlusConfig;
import com.atguigu.gmall.common.minio.minioConfig.MinioAutoConfiguration;
import com.atguigu.gmall.common.minio.service.impl.OssServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(MinioAutoConfiguration.class)
public @interface EnableMinio {
}
