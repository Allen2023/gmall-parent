package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 8:57
 */
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {
    /**
     * 根据分类id获取平台属性
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    List<BaseAttrInfo> getAttrInfoList(@Param("category1Id") Long category1Id, @Param("category2Id") Long category2Id, @Param("category3Id") Long category3Id);

    void saveAttrInfoAndValue(@Param("baseAttrInfo") BaseAttrInfo baseAttrInfo);
    void updateAttrInfoAndValue(@Param("baseAttrInfo") BaseAttrInfo baseAttrInfo);

    List<BaseAttrValue> findAttrValueById(Long attrId);

    void saveOrUpdateAttrInfoAndValue(BaseAttrInfo baseAttrInfo);

}
