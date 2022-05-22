package com.atguigu.gmall.item.service.Impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.product.ProductFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 23:32
 */
@Service
public class SkuDetailServiceImpl implements SkuDetailService {
    @Autowired
    ProductFeignClient productFeignClient;

    //商品详情服务：
    //查询sku详情得做这么多式
    //1、查分类
    //2、查Sku信息
    //3、查sku的图片列表
    //4、查价格
    //5、查所有销售属性组合
    //6、查实际sku组合
    //7、查介绍(不用管)
    @Override
    public SkuDetailTo getSkuDetial(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();
        //1、查分类
        Result<BaseCategoryView> skuCategoryView = productFeignClient.getSkuCategoryView(skuId);
        if (skuCategoryView.isOk()){
            skuDetailTo.setCategoryView(skuCategoryView.getData());
        }
        //2、查Sku信息


        return null;
    }
}
