package com.atguigu.gmall.item.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 23:36
 */
@Controller
@RequestMapping("/rpc/inner/item")
public class SkuDetailRpcController {

    @Autowired
    SkuDetailService skuDetailService;

    /**
     * 查询商品详情
     * @param skuId
     * @return
     */
    @GetMapping("/sku/detail/{skuId}")
    public Result<SkuDetailTo> getSkuDetial(@PathVariable("skuId") Long skuId) {
        SkuDetailTo skuDetial = skuDetailService.getSkuDetial(skuId);
        return Result.ok(skuDetial);
    }
}
