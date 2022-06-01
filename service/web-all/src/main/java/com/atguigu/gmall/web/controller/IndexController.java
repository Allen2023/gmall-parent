package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.model.to.CategoryAndChildTo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 19:24
 */
@Slf4j
@Controller
public class IndexController {
    @Autowired
    ProductFeignClient productFeignClient;

    //    classpath:/templates/index.html
    @GetMapping({"/","/index.com"})
    public String indexPage(Model model) {
//        log.info("首页");
        Result<List<CategoryAndChildTo>> result = productFeignClient.getAllCategoryWithChilds();
        if (result.isOk()) {
            //如果远程调用正常
            //拿到远程调用返回的数据
            List<CategoryAndChildTo> data = result.getData();
            model.addAttribute("list", data);
        }
        return "index/index";
    }


}
