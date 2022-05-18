package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.product.service.BaseAttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 8:56
 */
@RequestMapping("/admin/product")
@RestController
public class BaseAttrController {
    @Autowired
    BaseAttrService baseAttrService;

    /**
     * 根据分类id获取平台属性
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result getCategory3(@PathVariable(value = "category1Id") Long category1Id,
                               @PathVariable(value = "category2Id") Long category2Id,
                               @PathVariable(value = "category3Id") Long category3Id) {
        List<BaseAttrInfo> attrInfoList = baseAttrService.getAttrInfoList(category1Id,category2Id,category3Id);
        return Result.ok(attrInfoList);

    }
}
