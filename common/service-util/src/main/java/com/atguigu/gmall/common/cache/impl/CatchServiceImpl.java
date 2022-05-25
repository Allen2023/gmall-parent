package com.atguigu.gmall.common.cache.impl;

import com.atguigu.gmall.common.util.JSONs;
import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.atguigu.gmall.common.cache.CatchService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xsz
 * @Description: TODO
 * @DateTime: 2022/5/24 16:42
 */
@Service
public class CatchServiceImpl implements CatchService {


    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 获取缓存数据
     *
     * @param key
     * @param typeReference
     * @param <T>
     * @return
     */
    @Override
    public <T extends Object> T getCacheData(String key, TypeReference<T> typeReference) {
        //1.获取redis指定key的数据
        String json = redisTemplate.opsForValue().get(key);
        //2.判断json是否为null  不为null代表至少查过一次
        if (!StringUtils.isEmpty(json)) {
            //3.如果查询的为空值 则返回null
            if ("null".equals(json)) {
                T t = JSONs.nullInstance(typeReference);
                return t;
            }
            //4.将json数据转化为指定的对象
            T t = JSONs.strToObject(json, typeReference);
            return t;
        }
        //缓存中没数据,该数据一次都没有查过
        return null;
    }

    /**
     * 保存缓存数据
     *
     * @param key
     * @param data
     */
    @Override
    public void saveCatchData(String key, Object data) {
        if (data == null) {
            //数据库是 null long timeout, TimeUnit unit //被动型检查数据，缓存的短一点
            redisTemplate.opsForValue().set(key, "null", 30, TimeUnit.MINUTES);
        } else {
            //数据库有。 有的数据缓存的久一点
            //为了防止同时过期,给每个过期时间加上随机值
            Double v = Math.random() * 1000000000L;
            long millis = 1000 * 60 * 60 * 24 * 3 + v.intValue();
            redisTemplate.opsForValue().set(key, JSONs.toStr(data), millis, TimeUnit.DAYS);
        }
    }

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


}
