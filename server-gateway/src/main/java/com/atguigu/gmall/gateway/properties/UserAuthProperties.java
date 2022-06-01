package com.atguigu.gmall.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/31 19:42
 */
@Data
@ConfigurationProperties(prefix = "app.auth")
@Component
public class UserAuthProperties {

    List<String> anyoneurls;

    List<String> denyurls;

    List<String> authurls;

    String loginPage;
}
