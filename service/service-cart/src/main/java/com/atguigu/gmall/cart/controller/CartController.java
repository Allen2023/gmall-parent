package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 9:41
 */
@RequestMapping("/api/cart")
@RestController
public class CartController {

    @Autowired
    CartService cartService;

    /**
     * 获取购物车商品信息
     *
     * @return
     */
    @GetMapping("/cartList")
    public Result cartList() {
        List<CartItem> cartList = cartService.getCartItems();
        return Result.ok(cartList);
    }
}
