package com.atguigu.gmall.common.annotation;


import com.atguigu.gmall.common.exception.AppGlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Import({AppGlobalExceptionHandler.class})
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface EnableAutoHandleException {
}
