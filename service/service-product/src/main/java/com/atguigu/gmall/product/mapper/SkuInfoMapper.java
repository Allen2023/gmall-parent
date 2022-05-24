package com.atguigu.gmall.product.mapper;


import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author 86185
* @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
* @createDate 2022-05-19 11:23:11
* @Entity com.atguigu.gmall.product.domain.SkuInfo
*/
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    void upOrDownSku(Long skuId, int status);


    List<Long> getSkuIds();
}




