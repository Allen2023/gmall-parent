package com.atguigu.gmall.common.cache;

import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/24 16:41
 */
public interface CatchService {
    List<CategoryAndChildTo> getAllCategoryWithChilds();


    void saveData(List<CategoryAndChildTo> childs);

    /**
     * 获取缓存数据
     * @param key
     * @return
     */
   <T> T getCacheData(String key, TypeReference<T> typeReference);

    /**
     * 保存缓存数据
     * @param key
     * @param data
     */
    void saveCatchData(String key, Object data);
}
