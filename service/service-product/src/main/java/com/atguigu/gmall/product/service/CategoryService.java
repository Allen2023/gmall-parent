package com.atguigu.gmall.product.service;


import java.util.List;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryAndChildTo;

public interface CategoryService {

    List<BaseCategory1> getAllCategory1();

    List<BaseCategory2> getCategory2Byc1(Long category1Id);

    List<BaseCategory3> getCategory3Byc2(Long category2Id);


    List<CategoryAndChildTo> getAllCategoryWithChilds();


    BaseCategoryView getSkuCategoryView(Long skuId);
}
