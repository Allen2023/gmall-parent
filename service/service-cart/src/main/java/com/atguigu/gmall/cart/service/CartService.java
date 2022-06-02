package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartItem;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 19:05
 */
public interface CartService {
    /**
     * 获取购物车列表信息
     *
     * @return
     */
    List<CartItem> getCartItems();

    String determineCartKey();

    /**
     * 查询Redis中所有的商品信息
     *
     * @param skuId
     * @param skuNum
     * @return
     */
    CartItem addSkuToCart(Long skuId, Integer skuNum);


    CartItem saveSkuToCart(Long skuId, Integer skuNum,String key);


}
