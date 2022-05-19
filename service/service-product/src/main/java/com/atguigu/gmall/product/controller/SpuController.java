package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/19 9:49
 */
@RequestMapping("/admin/product")
@RestController
public class SpuController {

    @Autowired
    SpuInfoService spuInfoService;

    @Autowired
    BaseSaleAttrService baseAttrService;

    /**
     * 商品属性分页
     *
     * @param pageNum
     * @param pageSize
     * @param category3Id
     * @return
     */
    //admin/product/1/10?category3Id=2
    @GetMapping("/{pageNum}/{pageSize}")
    public Result getSpuPage(@PathVariable("pageNum") Long pageNum, @PathVariable("pageSize") Long pageSize,
                             @RequestParam("category3Id") Long category3Id) {

        Page<SpuInfo> page = new Page<>(pageNum, pageSize);
        Page<SpuInfo> result = spuInfoService.getSpuInfoPage(page, category3Id);
        return Result.ok(result);
    }


    @GetMapping("/baseSaleAttrList")
    public Result getBaseSaleAttrList() {
        List<BaseSaleAttr> baseSaleAttrList = baseAttrService.getBaseSaleAttrList();

        return Result.ok(baseSaleAttrList);
    }

    /**
     * 添加SPU
     *
     * @param spuInfo
     * @return
     */
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo) {
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    /**
     * 获取spu信息
     *
     * @param spuId
     * @return
     */
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable("spuId") Long spuId) {
        List<SpuSaleAttr> spuSaleAttrList = spuInfoService.getSpuSaleAttrList(spuId);
        return Result.ok(spuSaleAttrList);
    }
}
