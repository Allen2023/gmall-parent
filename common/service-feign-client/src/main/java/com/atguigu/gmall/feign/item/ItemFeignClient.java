package com.atguigu.gmall.feign.item;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 23:20
 */
@FeignClient("service-item")
@RequestMapping("/rpc/inner/item")
public interface ItemFeignClient {
    /**
     * 查询商品详情
     * @param skuId
     * @return
     */
    @GetMapping("/sku/detail/{skuId}")
     Result<SkuDetailTo> getSkuDetial(@PathVariable("skuId") Long skuId);

}
