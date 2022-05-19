package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 10:54
 */
@Api(tags = "品牌列表接口", description = "BaseTrademarkController")
@EnableTransactionManagement
@RequestMapping("/admin/product")
@RestController
public class BaseTrademarkController {

    @Autowired
    BaseTrademarkService baseTrademarkService;

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取品牌分页列表")
    @GetMapping("/baseTrademark/{pageNum}/{pageSize}")
    public Result getBaseTrademarkPage(@PathVariable Long pageNum,
                                       @PathVariable Long pageSize) {

        Page<BaseTrademark> page = new Page<>(pageNum, pageSize);
        Page<BaseTrademark> result = baseTrademarkService.page(page);
        return Result.ok(result);
    }

    /**
     * 获取品牌属性
     *
     * @return
     */
    @ApiOperation(value = "获取品牌属性")
    @GetMapping("/baseTrademark/getTrademarkList")
    public Result getTrademarkList() {
        List<BaseTrademark> baseTrademarkList = baseTrademarkService.getTrademarkList();
        return Result.ok(baseTrademarkList);
    }

    /**
     * 添加品牌
     *
     * @param baseTrademark
     * @return
     */
    @ApiOperation(value = "添加品牌")
    @Transactional
    @PostMapping("/baseTrademark/save")
    public Result saveBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.save(baseTrademark);
        return Result.ok();
    }

    /**
     * 修改品牌
     *
     * @param baseTrademark
     * @return
     */
    @ApiOperation(value = "修改品牌")
    @Transactional
    @PutMapping("/baseTrademark/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    /**
     * 根据Id获取品牌
     *
     * @return baseTrademark
     */
    @ApiOperation(value = "根据Id获取品牌")
    @GetMapping("/baseTrademark/get/{id}")
    public Result getBaseTrademarkById(@PathVariable(value = "id") Long id) {
        BaseTrademark baseTrademark = baseTrademarkService.getById(id);
        return Result.ok(baseTrademark);
    }

    /**
     * 根据id删除某个品牌
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除某个品牌")
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result removeBaseTrademarkById(@PathVariable(value = "id") Long id) {
        baseTrademarkService.removeById(id);
        return Result.ok();
    }


}
