package com.atguigu.gmall.common.minio.service.impl;


import com.atguigu.gmall.common.minio.minioConfig.MinioProperties;
import com.atguigu.gmall.common.minio.service.OssService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.UUID;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/18 21:03
 */
@Service
public class OssServiceImpl implements OssService {

    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioProperties minioProperties;



    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        //1.准备一个唯一文件名
        String filename = UUID.randomUUID().toString().replace("-", "") + "_" + file.getOriginalFilename();
        //2.文件流
        InputStream fis = file.getInputStream();
        //上传参数设置项
        PutObjectOptions options = new PutObjectOptions(fis.available(), -1);
        //设置请求头
        options.setContentType(file.getContentType());
        minioClient.putObject(minioProperties.getBucket(), filename, fis, options);

        String path = minioProperties.getEndpoint() + "/" + minioProperties.getBucket() + "/" + filename;
        return path;
    }
}
