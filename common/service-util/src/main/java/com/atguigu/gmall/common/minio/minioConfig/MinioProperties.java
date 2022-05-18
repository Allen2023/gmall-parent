package com.atguigu.gmall.common.minio.minioConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @Author: xsz
 * @Description: 封装Minio配置属性 TODO
 * @DateTime: 2022/5/19 1:27
 */
@ConfigurationProperties(prefix = "app.minio")
@Data
public class MinioProperties {

    String endpoint;

    String accesskey;

    String secretkey;

    String bucket;


}
