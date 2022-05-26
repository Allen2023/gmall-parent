package com.atguigu.gmall.item.service;

import com.atguigu.gmall.model.to.SkuDetailTo;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 23:31
 */
public interface SkuDetailService {


    SkuDetailTo getSkuDetail(Long skuId) throws InterruptedException;
}
