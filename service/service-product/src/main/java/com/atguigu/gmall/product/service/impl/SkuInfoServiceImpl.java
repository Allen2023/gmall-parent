package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SkuInfoService;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author 86185
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2022-05-19 11:23:11
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
        implements SkuInfoService {
    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    SkuImageMapper skuImageMapper;

    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Qualifier("skuIdBloom")
    @Autowired
    RBloomFilter<Object> skuIdBloom;

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //1.添加Sku信息
        skuInfoMapper.insert(skuInfo);

        Long id = skuInfo.getId();
        skuIdBloom.add(id);
        // 就算删了的，布隆说有，我们查询数据库结果为null，我们也会缓存null值。
        // 就算布隆误判或者真没，都不担心会跟数据库建立大量连接；

        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (skuAttrValueList.size() > 0) {
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                //2.添加SkuAttrValue信息
                skuAttrValueMapper.insert(skuAttrValue);
            }
        }
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (skuSaleAttrValueList.size() > 0) {
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                //3.添加SkuAttrValue信息
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            }
        }
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (skuImageList.size() > 0) {
            for (SkuImage skuImage : skuImageList) {
                //4.添加SkuImage信息
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insert(skuImage);
            }
        }
    }

    @Override
    public void upOrDownSku(Long skuId, int status) {
        skuInfoMapper.upOrDownSku(skuId, status);
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return skuInfoMapper.selectById(skuId).getPrice();

    }

    @Override
    public List<SpuSaleAttr> getSkudeSpuSaleAttrAndValue(Long skuId) {
        return spuSaleAttrMapper.getSkudeSpuSaleAttrAndValue(skuId);

    }

    @Override
    public List<Long> getAllSkuIds() {

        return skuInfoMapper.getSkuIds();
    }


}




