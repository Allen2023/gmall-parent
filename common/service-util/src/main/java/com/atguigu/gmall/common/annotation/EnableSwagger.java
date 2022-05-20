package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.config.SwaggerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(SwaggerConfig.class)
public @interface EnableSwagger {
}
