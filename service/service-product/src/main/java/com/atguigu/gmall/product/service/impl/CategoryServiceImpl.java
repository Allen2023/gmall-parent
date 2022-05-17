package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.mapper.CategoryMapper1;
import com.atguigu.gmall.product.mapper.CategoryMapper2;
import com.atguigu.gmall.product.mapper.CategoryMapper3;
import com.atguigu.gmall.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    @Override
    public List<BaseCategory1> getAllCategory1() {

        List<BaseCategory1> baseCategory1List = categoryMapper1.selectList(null);
        return baseCategory1List;
    }


    @Override
    public List<BaseCategory2> getCategory2Byc1(Long category1Id) {
        List<BaseCategory2> baseCategory2List = categoryMapper2.selectList(new LambdaQueryWrapper<BaseCategory2>().eq(BaseCategory2::getId,category1Id));
        return baseCategory2List;
    }

    @Override
    public List<BaseCategory3> getCategory3Byc2(Long category2Id) {
        List<BaseCategory3> baseCategory3List = categoryMapper3.selectList(new LambdaQueryWrapper<BaseCategory3>().eq(BaseCategory3::getId,category2Id));
        return baseCategory3List;
    }
}
