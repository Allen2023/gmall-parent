package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.BaseSaleAttr;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseSaleAttrMapper;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/27 18:16
 */
@SpringBootTest
public class Testone {


    /*@Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;*/

    @Autowired
    BaseTrademarkMapper mapper;

    @Test
    public void Test1() {
        BaseTrademark baseTrademark = mapper.selectById(13L);
        System.out.println("baseTrademark = " + baseTrademark);

        BaseTrademark baseTrademark1 = mapper.selectById(13L);
        System.out.println("baseTrademark1 = " + baseTrademark1);
    }
}
