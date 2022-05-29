package com.atguigu.gmall.list.service.impl;

import com.atguigu.gmall.list.dao.GoodsDao;
import com.atguigu.gmall.list.service.GoodsSearchService;
import com.atguigu.gmall.model.list.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/29 23:44
 */
@Service
public class GoodsSearchServiceImpl implements GoodsSearchService {
    @Autowired
    GoodsDao goodsDao;

    /**
     * 保存商品到ES
     * @param goods
     */
    @Override
    public void saveGoods(Goods goods) {
        goodsDao.save(goods);
    }

    /**
     * 下架商品从ES中删除
     * @param skuId
     */
    @Override
    public void deleteGoods(Long skuId) {
        goodsDao.deleteById(skuId);
    }
}
