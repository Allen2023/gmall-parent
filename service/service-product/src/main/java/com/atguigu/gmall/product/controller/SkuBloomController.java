package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.redisson.SkuBloomTask;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.cron.SkuIdBloomTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/25 18:50
 */
@RequestMapping("/admin/product")
@RestController
public class SkuBloomController {

    @Autowired
    SkuIdBloomTask skuIdBloomTask;

    @GetMapping("/rebuild")
    public Result RebuildBloom() {
        skuIdBloomTask.rebuildBloom();
        return Result.ok();
    }
}
