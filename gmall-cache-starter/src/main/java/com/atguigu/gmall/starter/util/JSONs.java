package com.atguigu.gmall.starter.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JSONs {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    //将对象转为String
    public static String toStr(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转换JSON的String异常:{}", obj);
        }
        return null;
    }

    //将String转为对象
    public static <T> T strToObject(String json, TypeReference<T> typeReference) {
        T t = null;
        try {
            t = objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("菜单JSON转对象异常:{}", typeReference);
        }
        return t;
    }

    //将返回一个泛型对象
    public static <T extends Object> T nullInstance(TypeReference<T> typeReference) {
        String json = "[]";
        T t = null;
        try {
            t = objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("空实例异常", typeReference);
        }
        return t;
    }
}
