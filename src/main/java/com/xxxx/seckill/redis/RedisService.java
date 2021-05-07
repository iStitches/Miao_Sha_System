package com.xxxx.seckill.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class RedisService {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    private <T> T stringToBean(String value, Class<T> clazz){
        try {
            if(value==null || value.length()<=0)
                return null;
            if(clazz==int.class || clazz==Integer.class)
                return (T)Integer.valueOf(value);
            else if(clazz==long.class || clazz==Long.class)
                return (T)Long.valueOf(value);
            else if(clazz==String.class)
                return (T)value;
            else
                return mapper.readValue(value.getBytes(),clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> String beanToString(T value){
        try {
            if(value == null)
                return null;
            Class<?> clazz = value.getClass();
            if(clazz==int.class || clazz==Integer.class)
                return ""+value;
            else if(clazz==long.class || clazz==Long.class)
                return ""+value;
            else if(clazz==String.class)
                return (String) value;
            else
                return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T get(KeyPrefix keyPrefix, String  key, Class<T> clazz){
        String realKey = keyPrefix.getPrefix()+key;
        Object res = redisTemplate.opsForValue().get(realKey);
        return (T)res;
    }

    public <T> boolean set(KeyPrefix keyPrefix, T key, Object value){
        String realKey = keyPrefix.getPrefix()+beanToString(key);
        long seconds = keyPrefix.expireSeconds();
        if(seconds==0 || seconds==-1)
            redisTemplate.opsForValue().set(realKey,value);
        else
            redisTemplate.opsForValue().set(realKey,value,seconds,TimeUnit.SECONDS);
        return true;
    }

    public boolean exists(KeyPrefix keyPrefix, String key){
        try {
            String realKey = keyPrefix.getPrefix()+key;
            return redisTemplate.hasKey(realKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long incr(KeyPrefix prefix, String key, long delta){
        if(delta < 0)
            throw new RuntimeException("递增因子必须大于0");
        String realKey = prefix.getPrefix()+key;
        return redisTemplate.opsForValue().increment(realKey, delta);
    }

    public long decr(KeyPrefix prefix, String key, long delta){
        if(delta < 0)
            throw new RuntimeException("递减因子必须大于0");
        String realKey = prefix.getPrefix()+key;
        return redisTemplate.opsForValue().increment(realKey, -delta);
    }

    public boolean delete(KeyPrefix prefix, String key){
        try {
            String realKey = prefix.getPrefix()+key;
            return redisTemplate.delete(realKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean expire(KeyPrefix prefix, String key, long time){
        try {
            String realKey = prefix.getPrefix()+key;
            if(time > 0)
                redisTemplate.expire(realKey,time,TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
