package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SpuImageService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/19 11:18
 */
@RequestMapping("/admin/product")
@RestController
public class SkuController {

    //http://api.gmall.com/admin/product/spuImageList/{spuId}
    @Autowired
    SpuImageService spuImageService;

    @Autowired
    SkuInfoService skuInfoService;

    /**
     * 根据id查询spuImage
     *
     * @param spuId
     * @return
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result getSpuImageList(@PathVariable("spuId") Long spuId) {
        List<SpuImage> spuImageList = spuImageService.getSpuImageList(spuId);
        return Result.ok(spuImageList);
    }

    /**
     * 商品属性
     * Sku分页
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list/{pageNum}/{pageSize}")
    public Result getSkuPage(@PathVariable("pageNum") Long pageNum, @PathVariable("pageSize") Long pageSize) {
        Page<SkuInfo> page = new Page<>(pageNum, pageSize);
        Page<SkuInfo> skuInfoPage = skuInfoService.page(page);
        return Result.ok(skuInfoPage);
    }


    /**
     * 添加Sku
     * @param skuInfo
     * @return
     */
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo) {
        skuInfoService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    /**
     *上架Sku
     * @return
     */
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.onSale(skuId);
        return Result.ok();
    }

    /**
     *下架Sku
     * @return
     */
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId) {
        skuInfoService.cancelSale(skuId);
        return Result.ok();
    }
}
