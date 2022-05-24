package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @author 86185
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2022-05-19 11:23:11
*/
public interface SkuInfoService extends IService<SkuInfo> {

    void saveSkuInfo(SkuInfo skuInfo);


    void upOrDownSku(Long skuId, int status);

    BigDecimal getSkuPrice(Long skuId);

    List<SpuSaleAttr> getSkudeSpuSaleAttrAndValue(Long skuId);


    List<Long> getAllSkuIds();

}
