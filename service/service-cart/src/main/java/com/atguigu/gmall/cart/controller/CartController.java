package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 9:41
 */
@RequestMapping("/api/cart")
@RestController
public class CartController {

    @GetMapping("/cartList")
    public Result cartList() {
        return Result.ok();
    }
}
