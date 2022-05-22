package com.atguigu.gmall.product.service;


import java.util.List;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.model.to.CategoryAndChildTo;

public interface CategoryService {

    List<BaseCategory1> getAllCategory1();

    List<BaseCategory2> getCategory2Byc1(Long category1Id);

    List<BaseCategory3> getCategory3Byc2(Long category2Id);


    List<CategoryAndChildTo> getAllCategoryWithChilds();


}
