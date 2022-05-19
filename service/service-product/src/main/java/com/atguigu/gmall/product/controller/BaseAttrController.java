package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 8:56
 */
@Api(description = "平台属性接口")
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
    @ApiOperation(value = "根据分类id获取平台属性")
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
    @ApiOperation(value = "保存平台属性")
    @PostMapping("/saveAttrInfo")
    public Result getCategory3(@RequestBody BaseAttrInfo baseAttrInfo) {
//        baseAttrService.saveAttrInfoAndValue(baseAttrInfo);
        baseAttrService.saveOrUpdateAttrInfoAndValue(baseAttrInfo);
        return Result.ok();
    }

    /**
     * 根据attrId获取属性值列表
     * @param attrId
     * @return
     */
    @ApiOperation(value = "根据attrId获取属性值列表")
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable(value = "attrId") Long attrId) {
        List<BaseAttrValue> attrValueList = baseAttrService.findAttrValueById(attrId);
        return Result.ok(attrValueList);
    }
}
