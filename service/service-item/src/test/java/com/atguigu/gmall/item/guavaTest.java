package com.atguigu.gmall.item;


import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/24 20:51
 */
public class guavaTest {
    /**
     * Funnel<? super T> funnel,
     * long expectedInsertions, 预估数据量
     * double fpp 误判率
     */
    @Test
    public void bloomTest() {
        Funnel<Integer> funnel = Funnels.integerFunnel();
        //  void funnel(T var1, PrimitiveSink var2);
        BloomFilter<Integer> filter = BloomFilter.create(funnel, 1000000, 0.0001);

        //往布隆过滤器中放入值
        filter.put(88);
        filter.put(100);
        filter.put(99);

        //判断是否有这个值
        boolean test = filter.test(88);
        System.out.println("test = " + test);//true

        //基于这个原理,防止随机值穿透攻击

    }
}
