package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 8:56
 */
@EnableTransactionManagement //开启事务
@RequestMapping("/admin/product")
@RestController
public class BaseAttrController {
    @Autowired
    BaseAttrInfoService baseAttrService;

    /**
     * 根据分类id获取平台属性
     *
     * @param category1Id
     * @param category2Id
     * @param category3Id
     * @return
     */
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result getAttrInfoList(@PathVariable(value = "category1Id") Long category1Id,
                                  @PathVariable(value = "category2Id") Long category2Id,
                                  @PathVariable(value = "category3Id") Long category3Id) {
        List<BaseAttrInfo> attrInfoList = baseAttrService.getAttrInfoList(category1Id, category2Id, category3Id);
        return Result.ok(attrInfoList);

    }

    /**
     * 保存平台属性
     * @param baseAttrInfo
     * @return
     */
    @PostMapping("/saveAttrInfo")
    public Result getCategory3(@RequestBody BaseAttrInfo baseAttrInfo) {
        baseAttrService.saveAttrInfoAndValue(baseAttrInfo);
        return Result.ok();
    }


    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable(value = "attrId") Long attrId) {
        List<BaseAttrValue> attrValueList = baseAttrService.findAttrValueById(attrId);
        return Result.ok(attrValueList);
    }
}
