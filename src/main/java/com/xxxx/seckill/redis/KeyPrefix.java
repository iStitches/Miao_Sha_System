package com.xxxx.seckill.redis;

public interface KeyPrefix {
    public long expireSeconds();

    public String getPrefix();
}
