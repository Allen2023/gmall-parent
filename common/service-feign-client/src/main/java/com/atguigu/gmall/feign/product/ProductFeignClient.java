package com.atguigu.gmall.feign.product;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 23:46
 */
@FeignClient("service-product")
@RequestMapping("/rpc/inner/product")
public interface ProductFeignClient {

    /**
     * 获取系统的所有分类以及子分类
     *
     * @return
     */
    @GetMapping("/categorys")
    Result<List<CategoryAndChildTo>> getAllCategoryWithChilds();

    /**
     * 获取一个sku的分类层级信息
     *
     * @return
     */
    @GetMapping("/category/view/{skuId}")
     Result<BaseCategoryView> getSkuCategoryView(@PathVariable("skuId") Long skuId);

    /**
     * 查询skuinfo信息
     *
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/{skuId}")
    Result<SkuInfo> getSkuInfo(@PathVariable("skuId") Long skuId);


    /**
     * 查询sku价格
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/price/{skuId}")
     Result<BigDecimal> getSkuPrice(@PathVariable("skuId") Long skuId);


    /**
     * //5、查所有销售属性组合
     * @param skuId
     * @return
     */
    @GetMapping("/skuInfo/spu/saleattrandvalues/{skuId}")
     Result<List<SpuSaleAttr>> getSkudeSpuSaleAttrAndValue(@PathVariable("skuId") Long skuId);

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
     Result<Map<String, String>> getSkuValueJson(@PathVariable("skuId") Long skuId);
}
