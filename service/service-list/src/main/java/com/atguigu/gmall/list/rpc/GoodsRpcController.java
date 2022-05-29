package com.atguigu.gmall.list.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.list.service.GoodsSearchService;
import com.atguigu.gmall.model.list.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/29 23:40
 */
@RequestMapping("/rpc/inner/es")
@RestController
public class GoodsRpcController {

    @Autowired
    GoodsSearchService goodsSearchService;

    /**
     * 上架商品,保存数据到es
     * @param goods
     * @return
     */
    @PostMapping("/goods/save")
    public Result saveGoods(@RequestBody Goods goods){
        goodsSearchService.saveGoods(goods);
        return Result.ok();
    }

    /**
     * 下架商品,从ES中删除数据
     * @param skuId
     * @return
     */
    @GetMapping("/goods/delete/{skuId}")
    public Result deleteGoods(@PathVariable("skuId") Long skuId){
        goodsSearchService.deleteGoods(skuId);
        return Result.ok();
    }
}
