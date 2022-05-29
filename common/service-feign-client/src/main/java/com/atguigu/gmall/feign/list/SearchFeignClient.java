package com.atguigu.gmall.feign.list;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/29 23:54
 */
@RequestMapping("/rpc/inner/es")
@FeignClient("service-list")
public interface SearchFeignClient {
    /**
     * 上架商品,保存数据到es
     *
     * @param goods
     * @return
     */
    @PostMapping("/goods/save")
    Result saveGoods(@RequestBody Goods goods);


    /**
     * 下架商品,从ES中删除数据
     *
     * @param skuId
     * @return
     */
    @GetMapping("/goods/delete/{skuId}")
    Result deleteGoods(@PathVariable("skuId") Long skuId);
}
