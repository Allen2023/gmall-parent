package com.atguigu.gmall.common.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/19 16:50
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     * @return
     */
    @Bean
    public Docket ProductApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(ProductApiInfo("尚品汇商城后台 APIs", "1.0"))
                .select()
                //.apis(RequestHandlerSelectors.basePackage("com.atguigu.gmall.product.controller"))
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }
    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://ip:port/swagger-ui.html
     * @return
     */
    private ApiInfo ProductApiInfo(String title, String version){
        return new ApiInfoBuilder()
                .title(title)
                .description("本文档描述了后台管理系统微服务接口定义")
                .contact(new Contact("Allen", "https://github.com/Allen2023", "1273343014@qq.com"))
                .version(version)
                .build();
    }
}
