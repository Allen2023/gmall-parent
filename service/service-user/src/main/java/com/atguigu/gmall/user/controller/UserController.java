package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.IpUtil;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.LoginUserResponseVo;
import com.atguigu.gmall.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/31 18:16
 */
@RequestMapping("/api/user")
@RestController
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/passport/login")
    public Result login(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        String ipAddress = IpUtil.getIpAddress(request);
        userInfo.setIpAddr(ipAddress);
        LoginUserResponseVo responseVo = userInfoService.login(userInfo);
        if (responseVo == null) {
            //登陆失败
            return Result.build("", ResultCodeEnum.LOGIN_ERROR);
        }
        return Result.ok(responseVo);
    }


    @GetMapping("/passport/logout")
    public Result logOut(@RequestHeader("token") String token) {
        userInfoService.logOut(token);
        return Result.ok();
    }


}
