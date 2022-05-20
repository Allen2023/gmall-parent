package com.atguigu.gmall.common.annotation;


import com.atguigu.gmall.common.minio.minioConfig.MinioAutoConfiguration;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(MinioAutoConfiguration.class)
public @interface EnableMinio {
}
