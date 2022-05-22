package com.atguigu.gmall.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 19:24
 */
@Slf4j
@Controller
public class IndexController {
//    classpath:/templates/index.html
    @GetMapping("/")
    public String indexPage(){
        log.info("首页");
        return "index/index";
    }
}
