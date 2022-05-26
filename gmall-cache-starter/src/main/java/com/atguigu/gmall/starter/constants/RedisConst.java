package com.atguigu.gmall.starter.constants;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/24 18:21
 */
public class RedisConst {
    public static final String CATEGORY_CACHE_KEY = "categorys";
    public static final String SKU_CACHE_KEY_PREFIX = "sku:detail:";
    public static final String BLOOM_SKU_ID = "BLOOM:skuId";
    public static final String LOCK_PREFIX = "lock:";
    public static final String SKUDETAIL_LOCK_PREFIX = "lock:detail:";
    public static final String SALE_ATTR_CACHE_KEY = "sale:attr:";
}
