package com.atguigu.gmall.product.mapper;


import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86185
* @description 针对表【spu_sale_attr_value(spu销售属性值)】的数据库操作Mapper
* @createDate 2022-05-19 10:44:37
* @Entity com.atguigu.gmall.product.domain.SpuSaleAttrValue
*/
public interface SpuSaleAttrValueMapper extends BaseMapper<SpuSaleAttrValue> {
    /**
     * 查询spuId对应的所有销售属性名和值
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrAndValue(Long spuId);
}




