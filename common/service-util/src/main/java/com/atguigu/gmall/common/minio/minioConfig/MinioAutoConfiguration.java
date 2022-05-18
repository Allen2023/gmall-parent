package com.atguigu.gmall.common.minio.minioConfig;

import com.atguigu.gmall.common.minio.service.OssService;
import com.atguigu.gmall.common.minio.service.impl.OssServiceImpl;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/19 1:25
 */
@EnableConfigurationProperties(MinioProperties.class)
@Configuration
public class MinioAutoConfiguration {

    @Autowired
    MinioProperties minioProperties;

    //minio配置
    @Bean
    public MinioClient minioClient() throws Exception {

        MinioClient minioClient = new MinioClient(minioProperties.getEndpoint(), minioProperties.getAccesskey(), minioProperties.getSecretkey());
        boolean exists = minioClient.bucketExists(minioProperties.getBucket());
        //如果桶bucket不存在则创建一个新桶
        if (!exists) {
            minioClient.makeBucket(minioProperties.getBucket());
        }
        return minioClient;
    }

    @Bean
    public OssService ossService() {
        OssServiceImpl service = new OssServiceImpl();
        //SpringBoot会自动发现 OssServiceImpl注入他想要的东西
        return service;
    }
}
