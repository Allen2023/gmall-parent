package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseTrademark;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author 86185
* @description 针对表【base_trademark(品牌表)】的数据库操作Service
* @createDate 2022-05-18 11:19:50
*/
public interface BaseTrademarkService extends IService<BaseTrademark> {


    List<BaseTrademark> getTrademarkList();

}
