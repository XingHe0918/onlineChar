package com.example.loginservice.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 设置值
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 获取值
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除键
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    // 设置过期时间
    public void setExpire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    // 检查键是否存在
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void setValueAndExpire(String key, Object value, long timeout, TimeUnit unit) {
        setValue(key, value);
        setExpire(key, timeout, unit);
    }

}
