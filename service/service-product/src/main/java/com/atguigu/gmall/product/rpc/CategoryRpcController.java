package com.atguigu.gmall.product.rpc;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategoryView;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.atguigu.gmall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 20:53
 * 规范:"/rpc/inner/product"
 * rpc开头表示这个类提供别人远程调用自己的能力
 * 包含inner路径的,代表这个接口是内部接口,禁止外部直接访问
 */
@RestController
@RequestMapping("/rpc/inner/product")
public class CategoryRpcController {

    @Autowired
    CategoryService categoryService;

    /**
     * 获取系统的所有分类以及子分类
     *
     * @return
     */
    @GetMapping("/categorys")
    public Result<List<CategoryAndChildTo>> getAllCategoryWithChilds() {
        List<CategoryAndChildTo> categorys = categoryService.getAllCategoryWithChilds();
        return Result.ok(categorys);
    }

    /**
     * 获取一个sku的分类层级信息
     *
     * @return
     */
    @GetMapping("/category/view/{skuId}")
    public Result<BaseCategoryView> getSkuCategoryView(@PathVariable("skuId") Long skuId) {
        BaseCategoryView view = categoryService.getSkuCategoryView(skuId);
        return Result.ok(view);
    }

}
