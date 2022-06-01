package com.atguigu.gmall.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 9:37
 */
@Slf4j
@Controller
public class CartController {

    /**
     * 购物车页面
     * @return
     */
    @GetMapping("/cart.html")
    public String cartPage() {
        return "cart/index";
    }


    /**
     * 添加购物车
     * @param skuId
     * @param skuNum
     * @return
     */
    @GetMapping("/addCart.html")//从前端传递 商品id 商品数量
    public String addCart(@RequestParam("skuId") Long skuId,
                          @RequestParam("skuNum") Integer skuNum) {
        return "cart/addCart";
    }
}
