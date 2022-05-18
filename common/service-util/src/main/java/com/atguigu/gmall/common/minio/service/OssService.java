package com.atguigu.gmall.common.minio.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 21:02
 */
public interface OssService {

    String uploadFile(MultipartFile file) throws Exception;

}
