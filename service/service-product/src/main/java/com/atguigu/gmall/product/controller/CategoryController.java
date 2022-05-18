package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/admin/product")
@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 查询一级分类
     *
     * @return
     */
    @GetMapping("/getCategory1")
    public Result getCategory() {
        List<BaseCategory1> category1s = categoryService.getAllCategory1();
        return Result.ok(category1s);
    }

    /**
     * 查询二级分类
     *
     * @return
     */
    @GetMapping("/getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable(value = "category1Id") Long category1Id) {
        List<BaseCategory2> category2s = categoryService.getCategory2Byc1(category1Id);
        return Result.ok(category2s);
    }

    /**
     * 查询三级分类
     *
     * @return
     */
    @GetMapping("/getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable(value = "category2Id") Long category2Id) {
        List<BaseCategory3> category3s = categoryService.getCategory3Byc2(category2Id);
        return Result.ok(category3s);
    }



}
