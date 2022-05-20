package com.atguigu.gmall.common.exception;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/20 15:49
 */

@Slf4j
@RestControllerAdvice//全局异常处理切面
public class AppGlobalExceptionHandler {
    @Value("${spring.application.name}")
    String applicationName;

    @ExceptionHandler(GmallException.class)
    public Result handleGmallException(GmallException e) {
        log.error("全局异常捕获:业务异常:{}", e);
        Result fail = Result.fail();
        fail.setCode(e.getCode());
        fail.setMessage(e.getMessage());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        //异常的常用信息
        //1、请求
        //2、参数
        //获取当前请求信息
        HttpServletRequest request = attributes.getRequest();
        StringBuffer requestURL = request.getRequestURL();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> requestInfoMap = new HashMap<>();
        requestInfoMap.put("path", requestURL);
        requestInfoMap.put("params", parameterMap);
        requestInfoMap.put("serviceName", applicationName);
        fail.setData(requestInfoMap);
        return fail;
    }

    @ExceptionHandler(Exception.class)
    public Result handleOtherException(Exception e) {
        log.error("全局异常捕获:系统异常:{}", e);
        Result fail = Result.fail();
        fail.setCode(500);
        fail.setMessage("服务器内部异常");
        return fail;
    }
}
