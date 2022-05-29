package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.feign.list.SearchFeignClient;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.domain.BaseCategoryView;
import com.atguigu.gmall.product.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SkuInfoService;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    SearchFeignClient searchFeignClient;

    @Autowired
    BaseTrademarkMapper baseTrademarkMapper;

    @Autowired
    BaseCategoryViewMapper baseCategoryViewMapper;

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

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

    /**
     * 上架和下架商品SKU
     * @param skuId
     * @param status
     */
    @Override
    public void upOrDownSku(Long skuId, int status) {

        skuInfoMapper.upOrDownSku(skuId, status);
        if (status==1){
            //status==1为上架
            Goods goods = this.getSkuInfoForSearch(skuId);
            searchFeignClient.saveGoods(goods);
        }
        if(status==0){
            //下架 从ES中删除该商品的数据
            searchFeignClient.deleteGoods(skuId);
        }
    }
    //查到当前sku的详细信息，封装成Goods
    @Override
    public Goods getSkuInfoForSearch(Long skuId) {
        Goods goods = new Goods();
        //1.查询sku信息,并封装进去
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        goods.setId(skuInfo.getId());
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setCreateTime(new Date());
        //2.封装品牌信息
        BaseTrademark trademark = baseTrademarkMapper.selectById(skuInfo.getTmId());
        goods.setTmId(trademark.getId());
        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());
        //3.封装分类信息
        LambdaQueryWrapper<BaseCategoryView> wrapper = new LambdaQueryWrapper<BaseCategoryView>()
                .eq(BaseCategoryView::getCategory3Id, skuInfo.getCategory3Id());
        BaseCategoryView categortView = baseCategoryViewMapper.selectOne(wrapper);
        goods.setCategory1Id(categortView.getCategory1Id());
        goods.setCategory1Name(categortView.getCategory1Name());
        goods.setCategory2Id(categortView.getCategory2Id());
        goods.setCategory2Name(categortView.getCategory2Name());
        goods.setCategory3Id(categortView.getCategory3Id());
        goods.setCategory3Name(categortView.getCategory3Name());
        //4.热度分
        goods.setHotScore(0L);

        //5.当前sku的所有平台属性的名和值
        List<SearchAttr> attrs = baseAttrInfoMapper.getSkuBaseAttrNameAndValue(skuId);
        goods.setAttrs(attrs);
        return goods;
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




