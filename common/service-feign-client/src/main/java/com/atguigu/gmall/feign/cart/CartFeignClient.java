package com.atguigu.gmall.feign.cart;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 19:29
 */
@RequestMapping("/rpc/inner/cart")
@FeignClient("service-cart")
public interface CartFeignClient {

    /**
     * 把商品添加到购物车
     * @param skuId 商品id
     * @param skuNum 添加的数量
     * @return
     */
    @GetMapping("/add/{skuId}")
    Result<CartItem> addSkuToCart(@PathVariable("skuId") Long skuId,
                                         @RequestParam("skuNum") Integer skuNum);
}
