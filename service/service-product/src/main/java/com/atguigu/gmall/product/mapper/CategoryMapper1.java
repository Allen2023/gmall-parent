package com.atguigu.gmall.product.mapper;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


public interface CategoryMapper1 extends BaseMapper<BaseCategory1> {

    List<CategoryAndChildTo> getAllCategoryWithChilds();

}
