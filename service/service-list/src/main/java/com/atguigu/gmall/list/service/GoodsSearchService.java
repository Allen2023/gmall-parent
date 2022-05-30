package com.atguigu.gmall.list.service;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/29 23:43
 */
public interface GoodsSearchService  {
    /**
     * 上架商品,保存到ES
     * @param goods
     */
    void saveGoods(Goods goods);

    /**
     * 下架商品,从ES中删除
     * @param skuId
     */
    void deleteGoods(Long skuId);

    /**
     * 检索商品
     * @param params
     * @return
     */
    GoodsSearchResultVo searchGoods(SearchParam params);

    /**
     * 增加商品热度
     * @param skuId
     * @param score
     */
    void updateHotScore(Long skuId, Long score);
}
