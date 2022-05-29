package com.atguigu.gmall.product.mapper;


import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseAttrInfoMapper extends BaseMapper<BaseAttrInfo> {
    List<BaseAttrInfo> selectBaseAttrInfoList( Long category1Id,  Long category2Id,  Long category3Id);


    /**
     *当前sku的所有平台属性的名和值
     * @param skuId
     * @return
     */
    List<SearchAttr> getSkuBaseAttrNameAndValue(@Param("skuId") Long skuId);
}
