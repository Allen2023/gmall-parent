package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.SpuImageMapper;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import com.atguigu.gmall.product.mapper.SpuSaleAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //1.添加spuInfo信息
        spuInfoMapper.insert(spuInfo);
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if (spuImageList.size() > 0) {
            for (SpuImage spuImage : spuImageList) {
                Long id = spuInfo.getId();
                spuImage.setSpuId(id);
        //2.添加SpuImage信息
                spuImageMapper.insert(spuImage);
            }
        }
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if (spuSaleAttrList.size() > 0) {
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                Long id = spuInfo.getId();
                spuSaleAttr.setSpuId(id);
         //3.添加SpuSaleAttr信息
                spuSaleAttrMapper.insert(spuSaleAttr);
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if (spuSaleAttrValueList.size() > 0) {
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        String saleAttrName = spuSaleAttr.getSaleAttrName();
                        spuSaleAttrValue.setSaleAttrName(saleAttrName);
                        Long spuId = spuInfo.getId();
                        spuSaleAttrValue.setSpuId(spuId);
                        //4.添加SpuSaleAttrValue信息
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    }
                }
            }
        }


    }


}




