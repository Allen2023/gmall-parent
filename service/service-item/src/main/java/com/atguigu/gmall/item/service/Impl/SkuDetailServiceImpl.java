package com.atguigu.gmall.item.service.Impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.feign.item.ItemFeignClient;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 23:32
 */
@Service
public class SkuDetailServiceImpl implements SkuDetailService {
    @Autowired
    ProductFeignClient productFeignClient;

    @Autowired
    ThreadPoolExecutor corePool;
    //商品详情服务：
    //查询sku详情得做这么多式
    //1、查分类
    //2、查Sku信息
    //3、查sku的图片列表
    //4、查价格
    //5、查所有销售属性组合
    //6、查实际sku组合
    //7、查介绍(不用管)

    /**
     * 缓存查询
     * @param skuId
     * @return
     */
    @Override
    public SkuDetailTo getSkuDetial(Long skuId) {

        return null;
    }
    public SkuDetailTo getSkuDetialFromDb(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();
        //异步 编排: 编组(管理)+排列组合(运行)
        CompletableFuture<Void> categoryTask = CompletableFuture.runAsync(() -> {
            //1、查分类
            Result<BaseCategoryView> skuCategoryView = productFeignClient.getSkuCategoryView(skuId);
            if (skuCategoryView.isOk()) {
                skuDetailTo.setCategoryView(skuCategoryView.getData());
            }
        }, corePool);

        CompletableFuture<Void> SkuTask = CompletableFuture.runAsync(() -> {
            //2、查Sku信息 照片
            Result<SkuInfo> skuInfo = productFeignClient.getSkuInfo(skuId);
            if (skuInfo.isOk()) {
                skuDetailTo.setSkuInfo(skuInfo.getData());
            }
        }, corePool);

        CompletableFuture<Void> PriceTask = CompletableFuture.runAsync(() -> {
            //3.查询sku价格
            Result<BigDecimal> skuPrice = productFeignClient.getSkuPrice(skuId);
            if (skuPrice.isOk()) {
                skuDetailTo.setPrice(skuPrice.getData());
            }
        }, corePool);
        CompletableFuture<Void> SpuSaleAttrTask = CompletableFuture.runAsync(() -> {
            //4、查所有销售属性组合
            Result<List<SpuSaleAttr>> skudeSpuSaleAttrAndValue = productFeignClient.getSkudeSpuSaleAttrAndValue(skuId);
            if (skudeSpuSaleAttrAndValue.isOk()){
                skuDetailTo.setSpuSaleAttrList(skudeSpuSaleAttrAndValue.getData());
            }
        }, corePool);
        CompletableFuture<Void> valueJsonTask = CompletableFuture.runAsync(() -> {
            //5、查实际sku组合
            Result<Map<String, String>> skuValueJson = productFeignClient.getSkuValueJson(skuId);
            if(skuValueJson.isOk()){
                Map<String, String> jsonData = skuValueJson.getData();
                skuDetailTo.setValuesSkuJson(JSONs.toStr(jsonData));
            }
        }, corePool);

        //allOf 返回的 CompletableFuture 总任务结束再往下
        CompletableFuture.allOf(categoryTask, SkuTask, PriceTask, SpuSaleAttrTask, valueJsonTask).join();

        return skuDetailTo;
    }


}
