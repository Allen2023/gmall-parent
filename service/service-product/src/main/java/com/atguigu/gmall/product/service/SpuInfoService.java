package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.SpuInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86185
* @description 针对表【spu_info(商品表)】的数据库操作Service
* @createDate 2022-05-19 09:54:14
*/
public interface SpuInfoService extends IService<SpuInfo> {

    Page<SpuInfo> getSpuInfoPage(Page<SpuInfo> page, Long category3Id);



}
