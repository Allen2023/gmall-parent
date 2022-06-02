package com.atguigu.gmall.web.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 21:25
 */
@Component
public class UserHeaderRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String userId = requestAttributes.getRequest().getHeader("UserId");
        if (userId != null) {
            requestTemplate.header("UserId", userId);
        }
        String userTempId = requestAttributes.getRequest().getHeader("UserTempId");
        if (userTempId != null) {
            requestTemplate.header("UserTempId", userTempId);
        }
    }
}
