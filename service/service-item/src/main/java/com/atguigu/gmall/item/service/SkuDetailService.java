package com.atguigu.gmall.item.service;

import com.atguigu.gmall.model.to.SkuDetailTo;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 23:31
 */
public interface SkuDetailService {

    /**
     * 查询当前skuId的详情数据
     * @param skuId
     * @return
     * @throws InterruptedException
     */
    SkuDetailTo getSkuDetail(Long skuId) throws InterruptedException;

    /**
     * 增加当前skuId的热度
     * @param skuId
     */
    void updateHotScore(Long skuId);
}
