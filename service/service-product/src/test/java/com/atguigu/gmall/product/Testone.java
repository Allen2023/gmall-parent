package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/27 18:16
 */
//@SpringBootTest
public class Testone {


    /*@Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;*/

    @Autowired
    BaseTrademarkMapper mapper;

    //@Test
    public void Test1() {
        BaseTrademark baseTrademark = mapper.selectById(12L);
        System.out.println("baseTrademark = " + baseTrademark);

        BaseTrademark baseTrademark1 = mapper.selectById(12L);
        System.out.println("baseTrademark1 = " + baseTrademark1);
    }
}
