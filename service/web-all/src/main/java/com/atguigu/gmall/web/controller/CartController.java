package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 9:37
 */
@Slf4j
@Controller
public class CartController {

    @Autowired
    CartFeignClient cartFeignClient;

    /**
     * 购物车页面
     *
     * @return
     */
    @GetMapping("/cart.html")
    public String cartPage() {
        return "cart/index";
    }

    //http://cart.gmall.com/addCart.html?skuId=41&skuNum=1&sourceType=query

    /**
     * 添加购物车
     *
     * @param skuId
     * @param skuNum
     * @return
     */
    @GetMapping("/addCart.html")//从前端传递 商品id 商品数量
    public String addCart(@RequestParam("skuId") Long skuId,
                          @RequestParam("skuNum") Integer skuNum,
                          Model model, HttpServletRequest request) {
        Result<CartItem> result = cartFeignClient.addSkuToCart(skuId, skuNum);
        if (result.isOk()) {
            model.addAttribute("skuInfo", result.getData());
            model.addAttribute("skuNum", result.getData().getSkuNum());
        }
        return "cart/addCart";
    }
}
