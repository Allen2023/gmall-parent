package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 10:54
 */
@RequestMapping("/admin/product")
@RestController
public class BaseTrademarkController {
    @GetMapping("/baseTrademark/{pageNum}/{pageSize}")
    public Result getBaseTrademarkPage(@PathVariable String pageNum,
                                       @PathVariable String pageSize) {
    return Result.ok();
    }
}
