package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.atguigu.gmall.product.service.CatchService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/24 16:42
 */
@Service
public class CatchServiceImpl implements CatchService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public List<CategoryAndChildTo> getAllCategoryWithChilds() {
        //1.查询redis里的缓存数据  [序列化]
        String categorys = redisTemplate.opsForValue().get("categorys");
        //2.redis没有缓存这个key的数据
        if (StringUtils.isEmpty(categorys)) {
            return null;
        }
        //3.redis里有数据 [反序列化]
        List<CategoryAndChildTo> categoryAndChildToList = JSONs.strToCategoryObject(categorys);
        return categoryAndChildToList;
    }

    @Override
    public void saveData(List<CategoryAndChildTo> childs) {
        //1.将对象转化成String
        String strchilds = JSONs.toStr(childs);
        //存入redis
        redisTemplate.opsForValue().set("categorys", strchilds);
    }

    /**
     * 获取缓存数据
     *
     * @param categoryCacheKey
     * @param typeReference
     * @param <T>
     * @return
     */
    @Override
    public <T> T getCacheData(String categoryCacheKey, TypeReference<T> typeReference) {
        //获取redis指定key的数据
        String json = redisTemplate.opsForValue().get(categoryCacheKey);
        //判断json是否为null
        if (!StringUtils.isEmpty(json)) {
            //将json数据转化为指定的对象
            T t = JSONs.strToObject(json, typeReference);
            return t;
        }
        return null;
    }

    /**
     * 保存缓存数据
     *
     * @param categoryCacheKey
     * @param data
     */
    @Override
    public void saveCatchData(String categoryCacheKey, Object data) {

        redisTemplate.opsForValue().set(categoryCacheKey, JSONs.toStr(data));
    }
}
