package com.atguigu.gmall.product.service.impl;


import com.atguigu.gmall.common.cache.aop.annotation.Cache;
import com.atguigu.gmall.common.constants.RedisConst;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.atguigu.gmall.product.mapper.CategoryMapper1;
import com.atguigu.gmall.product.mapper.CategoryMapper2;
import com.atguigu.gmall.product.mapper.CategoryMapper3;
import com.atguigu.gmall.common.cache.CatchService;
import com.atguigu.gmall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper1 categoryMapper1;

    @Autowired
    CategoryMapper2 categoryMapper2;

    @Autowired
    CategoryMapper3 categoryMapper3;

    @Autowired
    CatchService catchService;


    @Override
    public List<BaseCategory1> getAllCategory1() {
        List<BaseCategory1> baseCategory1List = categoryMapper1.selectList(null);
        return baseCategory1List;
    }


    @Override
    public List<BaseCategory2> getCategory2Byc1(Long category1Id) {
        QueryWrapper<BaseCategory2> wrapper = new QueryWrapper<BaseCategory2>().eq("category1_id", category1Id);
        List<BaseCategory2> baseCategory2List = categoryMapper2.selectList(wrapper);
        return baseCategory2List;
    }

    @Override
    public List<BaseCategory3> getCategory3Byc2(Long category2Id) {
        QueryWrapper<BaseCategory3> wrapper = new QueryWrapper<BaseCategory3>().eq("category2_id", category2Id);
        List<BaseCategory3> baseCategory3List = categoryMapper3.selectList(wrapper);
        return baseCategory3List;
    }
    @Cache(cacheKey = RedisConst.CATEGORY_CACHE_KEY)
    @Override
    public List<CategoryAndChildTo> getAllCategoryWithChilds() {
        //1.查询缓存
        Object catchData = catchService.getCacheData(RedisConst.CATEGORY_CACHE_KEY, new TypeReference<List<CategoryAndChildTo>>() {
        });
        if (catchData == null) {
            //2.缓存没有值查询数据库
            List<CategoryAndChildTo> childs = categoryMapper1.getAllCategoryWithChilds();
            //3.将数据放入缓存
            catchService.saveCatchData(RedisConst.CATEGORY_CACHE_KEY,childs);
            return childs;
        } else {
            return (List<CategoryAndChildTo>) catchData;
        }
    }


    @Override
    public BaseCategoryView getSkuCategoryView(Long skuId) {
        return categoryMapper1.getSkuCategoryView(skuId);
    }
}
