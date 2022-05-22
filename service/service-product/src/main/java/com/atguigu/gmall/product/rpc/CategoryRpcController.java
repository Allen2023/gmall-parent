package com.atguigu.gmall.product.rpc;

import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 获取系统的所有分类以及子分类
     * @return
     */
    @GetMapping("/categorys")
    public Result getAllCategoryWithChilds(){
        return Result.ok();
    }
}
