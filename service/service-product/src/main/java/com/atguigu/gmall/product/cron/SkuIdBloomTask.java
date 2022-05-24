package com.atguigu.gmall.product.cron;

import com.atguigu.gmall.common.redisson.BloomTask;
import com.atguigu.gmall.common.redisson.SkuBloomTask;
import com.atguigu.gmall.product.service.SkuInfoService;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/25 1:42
 */
@Component
public class SkuIdBloomTask implements SkuBloomTask {
    @Autowired
    SkuInfoService skuInfoService;



    //秒 分 时 日 月 周 MON_FRI
    @Scheduled(cron = "0 0 3 * * 3")
    public void rebuildBloom() {
        //重建布隆
    }

    /**
     * 初始化SKU的布隆过滤器
     * 1.去数据库查询到所有的skuId ,然后添加到布隆中
     */
    @Override
    public void initData(RBloomFilter<Object> skuBloom) {
        //初始化布隆  查出所有的skuId放入布隆
        List<Long> ids = skuInfoService.getAllSkuIds();
        for (Long id : ids) {
            skuBloom.add(id);
        }
    }
}
