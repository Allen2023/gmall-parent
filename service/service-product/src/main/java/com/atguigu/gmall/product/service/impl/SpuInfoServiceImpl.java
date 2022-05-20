package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.SpuImageMapper;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import com.atguigu.gmall.product.mapper.SpuSaleAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 86185
 * @description 针对表【spu_info(商品表)】的数据库操作Service实现
 * @createDate 2022-05-19 09:54:14
 */
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
        implements SpuInfoService {

    @Autowired
    SpuInfoMapper spuInfoMapper;

    @Autowired
    SpuImageMapper spuImageMapper;

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public Page<SpuInfo> getSpuInfoPage(Page<SpuInfo> page, Long category3Id) {
        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("category3_id", category3Id);
        Page<SpuInfo> spuInfoPage = spuInfoMapper.selectPage(page, wrapper);
        return spuInfoPage;
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //1.添加spuInfo信息
        spuInfoMapper.insert(spuInfo);
        Long id = spuInfo.getId();
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if (spuImageList.size() > 0) {
            for (SpuImage spuImage : spuImageList) {

                spuImage.setSpuId(id);
                //2.添加SpuImage信息
                spuImageMapper.insert(spuImage);
            }
        }
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if (spuSaleAttrList.size() > 0) {
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                spuSaleAttr.setSpuId(id);
                //3.添加SpuSaleAttr信息
                spuSaleAttrMapper.insert(spuSaleAttr);
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if (spuSaleAttrValueList.size() > 0) {
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                        spuSaleAttrValue.setSpuId(id);
                        //4.添加SpuSaleAttrValue信息
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    }
                }
            }
        }


    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {
       /* //1.查询SpuSaleAttr
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.selectList(new LambdaQueryWrapper<SpuSaleAttr>().eq(SpuSaleAttr::getSpuId, spuId));
        //2.查询SpuSaleAttrValue
        List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttrValueMapper.selectList(new LambdaQueryWrapper<SpuSaleAttrValue>().eq(SpuSaleAttrValue::getSpuId, spuId));
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
            spuSaleAttr.setSpuSaleAttrValueList(spuSaleAttrValueList);
        }
        return spuSaleAttrList;*/

        List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrValueMapper.getSpuSaleAttrAndValue(spuId);
        return spuSaleAttrs;
    }


}




