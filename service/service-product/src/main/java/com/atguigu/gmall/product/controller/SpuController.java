package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/19 9:49
 */
@Api(tags = "spu接口", description = "SpuController")
@RequestMapping("/admin/product")
@EnableTransactionManagement
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
    @ApiOperation("获取spu分页列表")
    @GetMapping("/{pageNum}/{pageSize}")
    public Result getSpuPage(@PathVariable("pageNum") Long pageNum, @PathVariable("pageSize") Long pageSize,
                             @RequestParam("category3Id") Long category3Id) {

        Page<SpuInfo> page = new Page<>(pageNum, pageSize);
        Page<SpuInfo> result = spuInfoService.getSpuInfoPage(page, category3Id);
        return Result.ok(result);
    }

    /**
     * 获取销售属性
     *
     * @return
     */
    @ApiOperation("获取销售属性")
    @GetMapping("/baseSaleAttrList")
    public Result getBaseSaleAttrList() {
        List<BaseSaleAttr> baseSaleAttrList = baseAttrService.getBaseSaleAttrList();

        return Result.ok(baseSaleAttrList);
    }

    /**
     * 添加spu
     *
     * @param spuInfo
     * @return
     */
    @ApiOperation("添加spu")
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo) {
        spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    /**
     * 获取品牌属性
     *
     * @param spuId
     * @return
     */
    @ApiOperation("获取品牌属性")
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result getSpuSaleAttrList(@PathVariable("spuId") Long spuId) {
        List<SpuSaleAttr> spuSaleAttrList = spuInfoService.getSpuSaleAttrList(spuId);
        return Result.ok(spuSaleAttrList);
    }
}
