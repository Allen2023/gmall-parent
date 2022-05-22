package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;

import com.atguigu.gmall.feign.item.ItemFeignClient;
import com.atguigu.gmall.model.to.SkuDetailTo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 22:46
 */
@Slf4j
@Controller
public class ItemController {

    @Autowired
    ItemFeignClient itemFeignClient;

    @GetMapping("/{skuId}.html")
    public String itemPage(@PathVariable("skuId") Long skuId, Model model) {
        //log.info("商品详情：{}", skuId);
        //RPC查询数据
       Result<SkuDetailTo> detail = itemFeignClient.getSkuDetial(skuId);
       if (detail.isOk()){
           SkuDetailTo data = detail.getData();
           //1.当前sku所在分类的完整信息
           //category1Id、category1Name
           //category2Id、category2Name
           //category3Id、category3Name


           //2、当前sku基本信息
           //   skuId、skuName、skuDefaultImg、skuImageList

           //3、sku的价格
           //${spuSaleAttrList}; 定义的所有版本

           //4、sku的spu所有销售属性集合；
           //${valuesSkuJson}; 所有实际存在的sku组合。可切换的组合

           // {“118|120”:”49”,”119|120”:”50”}
           //5、当前spu可用的所有sku销售属性组合
       }



        return "item/index";
    }
}
