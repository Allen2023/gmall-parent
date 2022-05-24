package com.atguigu.gmall.common.redisson;

import org.redisson.api.RBloomFilter;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/25 2:01
 */
public interface BloomTask {
    /**
     * 初始化数据
     */
    void initData(RBloomFilter<Object> skuBloom);
}
