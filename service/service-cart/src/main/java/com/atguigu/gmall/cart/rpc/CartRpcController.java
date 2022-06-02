package com.atguigu.gmall.cart.rpc;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 19:14
 */
@RequestMapping("/rpc/inner/cart")
@RestController
public class CartRpcController {
    @Autowired
    CartService cartService;

    @GetMapping("/add/{skuId}")
    public Result<CartItem> addSkuToCart(@PathVariable("skuId") Long skuId,
                                         @RequestParam("skuNum") Integer skuNum) {
        CartItem cartItem = cartService.addSkuToCart(skuId, skuNum);

        return Result.ok(cartItem);
    }
}
