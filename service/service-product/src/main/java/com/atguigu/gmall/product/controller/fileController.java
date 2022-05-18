package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.minio.service.OssService;
import com.atguigu.gmall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin/product")
@RestController
public class fileController {

    @Autowired
    OssService ossService;

    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file) throws Exception {

        String path = ossService.uploadFile(file);
        return Result.ok(path);
    }
}
