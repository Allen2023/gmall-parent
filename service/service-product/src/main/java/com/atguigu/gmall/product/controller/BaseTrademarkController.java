package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 10:54
 */
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
    @GetMapping("/baseTrademark/{pageNum}/{pageSize}")
    public Result getBaseTrademarkPage(@PathVariable Long pageNum,
                                       @PathVariable Long pageSize) {


        Page<BaseTrademark> page = new Page<>(pageNum, pageSize);
        Page<BaseTrademark> result = baseTrademarkService.page(page);
        return Result.ok(result);
    }

    /**
     * 增加品牌
     *
     * @param baseTrademark
     * @return
     */
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
    @PutMapping("/baseTrademark/update")
    public Result updateBaseTrademark(@RequestBody BaseTrademark baseTrademark) {
        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    /**
     * 查询某个品牌
     *
     * @return baseTrademark
     */
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
    @DeleteMapping("/baseTrademark/remove/{id}")
    public Result removeBaseTrademarkById(@PathVariable(value = "id") Long id) {
        baseTrademarkService.removeById(id);
        return Result.ok();
    }


}
