package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.minio.service.OssService;
import com.atguigu.gmall.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 20:49
 */
@Slf4j
@Api(description = "文件上传接口")
@EnableTransactionManagement
@RequestMapping("/admin/product")
@RestController
public class fileController {

    @Autowired
    OssService ossService;


    @ApiOperation("文件上传")
    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file) throws Exception {
        String path = ossService.uploadFile(file);
        return Result.ok(path);
    }
}
