package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.constants.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthUtil;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.model.cart.CartItem;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.UserIdTo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/6/1 19:05
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductFeignClient productFeignClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    /**
     * 添加商品到购物车
     *
     * @param skuId
     * @param skuNum
     * @return
     */
    @Override
    public CartItem addSkuToCart(Long skuId, Integer skuNum) {
        //1.根据用户id决定使用哪个购物车键
        String cartKey = determineCartKey();
        //2.保存商品信息到Redis
        CartItem cartItem = saveSkuToCart(skuId, skuNum, cartKey);
        //3.返回保存的商品信息
        return cartItem;
    }

    /**
     * 1.根据用户id决定使用哪个购物车键
     *
     * @return
     */
    @Override
    public String determineCartKey() {
        String prefix = RedisConst.CART_KEY_PREFIX;
        //获取前端的老请求 //将前端传入的userId和userTempId放入openfeign远程调用中
        //通过拦截器 因为openfeign源码中把请求头去掉了,得自己加
        UserIdTo userAuth = AuthUtil.getUserAuth();
        if (userAuth.getUserId() != null) {
            //用户登录
            return prefix + userAuth.getUserId();
        } else {
            return prefix + userAuth.getUserTempId();
        }
    }

    @Override
    public CartItem saveSkuToCart(Long skuId, Integer skuNum, String key) {
        //1.绑定一个指定购物车的操作
        BoundHashOperations<String, String, String> cart = stringRedisTemplate.boundHashOps(key);

        //2.把skuId存到购物车
        //2.判断当前商品在Redis中有没有存在过
        Boolean hasKey = cart.hasKey(skuId.toString());
        if (!hasKey) {
            //远程调用product服务,拿到该商品的详细信息
            Result<SkuInfo> skuInfo = productFeignClient.getSkuInfo(skuId);

            //制作一个CartItem
            CartItem cartItem = convertSkuInfoToCartItem(skuInfo.getData(),skuNum);

            String cartItemStr = JSONs.toStr(cartItem);
            cart.put(skuId.toString(), cartItemStr);
            return cartItem;
        } else {
            //2.2如果这个存过,只是数量的增加
            String cartItemStr = cart.get(skuId.toString());
            CartItem cartItem = JSONs.strToObject(cartItemStr, new TypeReference<CartItem>() {
            });
            //设置新数量
            cartItem.setSkuNum(cartItem.getSkuNum() + skuNum);
            //更新数量
            cart.put(skuId.toString(), JSONs.toStr(cartItem));

            return cartItem;
        }
    }

    /**
     * 制作一个CartItem
     *
     * @param data
     * @return
     */
    private CartItem convertSkuInfoToCartItem(SkuInfo data,Integer skuNum) {
        CartItem cartItem = new CartItem();
        cartItem.setId(data.getId());
        //获取前端的老请求 //将前端传入的userId和userTempId放入openfeign远程调用中
        //通过拦截器 因为openfeign源码中把请求头去掉了,得自己加
        UserIdTo userAuth = AuthUtil.getUserAuth();
        if (userAuth.getUserId() != null) {
            cartItem.setUserId(userAuth.getUserId().toString());
        } else {
            cartItem.setUserId(userAuth.getUserTempId());
        }
        cartItem.setSkuId(data.getId());
        cartItem.setSkuNum(skuNum);
        cartItem.setSkuDefaultImg(data.getSkuDefaultImg());
        cartItem.setSkuName(data.getSkuName());
        cartItem.setIsChecked(1);
        cartItem.setCreateTime(new Date());
        cartItem.setUpdateTime(new Date());
        //第一次放进购物车的价格
        cartItem.setCartPrice(data.getPrice());
        //实时价格
        cartItem.setSkuPrice(data.getPrice());
        return cartItem;
    }


    /**
     * 查询redis中所有的商品信息
     *
     * @return
     */
    @Override
    public List<CartItem> getCartItems() {
        //1.得到购物车的键
        String cartKey = determineCartKey();
        //获取购物车的所有商品信息
        List<CartItem> cartItemList = getAllCartItem(cartKey);

        return cartItemList;
    }

    /**
     * 获取购物车中的商品信息
     *
     * @param cartKey
     * @return
     */
    private List<CartItem> getAllCartItem(String cartKey) {

        BoundHashOperations<String, String, String> cart = stringRedisTemplate.boundHashOps(cartKey);

        List<String> cartValues = cart.values();

        List<CartItem> cartItemList = null;
        if (cartValues != null) {
            cartItemList = cartValues.stream().map((jsonStr) -> {
                        CartItem cartItem = JSONs.strToObject(jsonStr, new TypeReference<CartItem>() {
                        });
                        return cartItem;
                    }).sorted((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()))
                    .collect(Collectors.toList());
        }
        return cartItemList;

    }

}
