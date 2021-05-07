package com.xxxx.seckill.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JSONUtil {
    private static ObjectMapper mapper = new ObjectMapper();


    public static String beanToString(Object object){
        if(object == null)
            return null;
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static <T> T stringToBean(String str, Class<T> clazz){
        if(str==null || str.length()<=0)
            return null;
        if(clazz==int.class || clazz==Integer.class)
            return (T) Integer.valueOf(str);
        else if(clazz==long.class || clazz==Long.class)
            return (T)Long.valueOf(str);
        else if(clazz==String.class)
            return (T)str;
        else
            try {
                return mapper.readValue(str.getBytes(),clazz);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
    }

    public static <T> List<T> stringToArray(String str, Class<T> clazz){
        if(str==null || str.length()<=0)
            return null;
        try {
            List<T> res = (List<T>)mapper.readValue(str, mapper.getTypeFactory().constructParametricType(List.class, clazz));
            return res;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
