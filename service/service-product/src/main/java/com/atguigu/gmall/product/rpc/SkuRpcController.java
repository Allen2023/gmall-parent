package com.atguigu.gmall.product.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.SkuSaleAttrValueMapper;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/23 0:22
 */
@RestController
@RequestMapping("/rpc/inner/product")
public class SkuRpcController {
    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImageService skuImageService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    //1、查分类
    //2、查Sku信息
    //3、查sku的图片列表
    //4、查价格
    //5、查所有销售属性组合
    //6、查实际sku组合
    //7、查介绍(不用管)

    /**
     * 查询skuinfo信息
     *
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId) {
        //查询skuInfo信息
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        //查询SkuImageList信息
        List<SkuImage> skuImageList = skuImageService.list(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId, skuId));
        skuInfo.setSkuImageList(skuImageList);
        return Result.ok(skuInfo);
    }

    /**
     * 查询sku价格
     *
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/price/{skuId}")
    public Result<BigDecimal> getSkuPrice(@PathVariable("skuId") Long skuId) {
        //4、查价格
        BigDecimal bigDecimal = skuInfoService.getSkuPrice(skuId);
        return Result.ok(bigDecimal);
    }

    /**
     * //5、查所有销售属性组合
     *
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/spu/saleattrandvalues/{skuId}")
    public Result<List<SpuSaleAttr>> getSkudeSpuSaleAttrAndValue(@PathVariable("skuId") Long skuId) {
        //5、查所有销售属性组合
        List<SpuSaleAttr> spuSaleAttrList = skuInfoService.getSkudeSpuSaleAttrAndValue(skuId);
        return Result.ok(spuSaleAttrList);
    }

    /**
     * 6、查实际sku组合
     * * 查询skuValueJson数据
     * * {
     * * “118|120”:”49”,
     * * ”119|120”:”50”
     *
     * @param skuId
     * @return
     */
    @GetMapping("/skuinfo/valuejson/{skuId}")
    public Result<Map<String, String>> getSkuValueJson(@PathVariable("skuId") Long skuId) {
        //6、查实际sku组合
        Map<String, String> valuesSkuJson = skuSaleAttrValueService.getSkuValueJson(skuId);
        return Result.ok(valuesSkuJson);
    }
}

