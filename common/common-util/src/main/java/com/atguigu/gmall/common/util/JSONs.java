package com.atguigu.gmall.common.util;

import com.atguigu.gmall.model.to.CategoryAndChildTo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JSONs {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String toStr(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("对象转换JSON的String异常:{}", obj);
        }
        return null;
    }

    public static List<CategoryAndChildTo> strToCategoryObject(String categorys) {
        List<CategoryAndChildTo> tos = null;
        try {
            tos = objectMapper.readValue(categorys, new TypeReference<List<CategoryAndChildTo>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("菜单JSON转对象异常:{}", categorys);
        }
        return tos;
    }


    public static <T> T strToObject(String json, TypeReference<T> typeReference) {
        T t = null;
        try {
            t = objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("菜单JSON转对象异常:{}", typeReference);
        }
        return t;
    }
}
