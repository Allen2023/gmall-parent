package com.atguigu.gmall.common.util;


import com.atguigu.gmall.model.to.UserIdTo;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthUtil {

    /**
     * 从当前请求中得到用户信息
     * @return
     */
    public static UserIdTo getUserAuth(){
        //获取老请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();

        String userId = attributes.getRequest().getHeader("UserId");
        String userTempId = attributes.getRequest().getHeader("UserTempId");

        UserIdTo authTo = new UserIdTo();
        if(!StringUtils.isEmpty(userId)){
            authTo.setUserId(Long.parseLong(userId));
        }

        if(!StringUtils.isEmpty(userTempId)){
            authTo.setUserTempId(userTempId);
        }

        return authTo;
    }
}
