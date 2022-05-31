package com.atguigu.gmall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/31 19:10
 */
@Controller
public class LoginController {

    /**
     * 展示登录页面
     * @param originUrl 登录成功以后回调页面
     * @param model
     * @return
     */
    @GetMapping("/login.html")
    public String loginPage(@RequestParam("originUrl") String originUrl, Model model) {
        model.addAttribute("originUrl", originUrl);
        return "login";
    }
}
