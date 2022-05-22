package com.atguigu.gmall.web.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/22 22:17
 */
@FeignClient("service-product")
@RequestMapping("/rpc/inner/product")
public interface ProductFeignClient {

    /**
     * 获取系统的所有分类以及子分类
     *
     * @return
     */
    @GetMapping("/categorys")
     Result<List<CategoryAndChildTo>> getAllCategoryWithChilds();
}
