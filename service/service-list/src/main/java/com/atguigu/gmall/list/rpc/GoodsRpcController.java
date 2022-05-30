package com.atguigu.gmall.list.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.list.service.GoodsSearchService;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.vo.GoodsSearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
     *
     * @param goods
     * @return
     */
    @PostMapping("/goods/save")
    public Result saveGoods(@RequestBody Goods goods) {
        goodsSearchService.saveGoods(goods);
        return Result.ok();
    }

    /**
     * 下架商品,从ES中删除数据
     *
     * @param skuId
     * @return
     */
    @GetMapping("/goods/delete/{skuId}")
    public Result deleteGoods(@PathVariable("skuId") Long skuId) {
        goodsSearchService.deleteGoods(skuId);
        return Result.ok();
    }

    /**
     * 检索商品
     * @param params
     * @return
     */
    @PostMapping("/goods/search")
    public Result<GoodsSearchResultVo> searchGoods(@RequestBody SearchParam params) {
        GoodsSearchResultVo vo = goodsSearchService.searchGoods(params);
        return Result.ok(vo);
    }

    @GetMapping("/goods/incrHotScore/{skuId}")
    public Result incrHotScore(@PathVariable("skuId") Long skuId,@RequestParam("hotScore") Long score) {
        goodsSearchService.updateHotScore(skuId, score);
        return Result.ok();
    }

}
