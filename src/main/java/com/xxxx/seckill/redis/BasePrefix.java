package com.xxxx.seckill.redis;

public abstract class BasePrefix implements KeyPrefix{
    private long expireSeconds;

    private String prefix;

    BasePrefix(long seconds, String prefix){
        this.expireSeconds = seconds;
        this.prefix = prefix;
    }

    BasePrefix(String prefix){
        this.expireSeconds = -1L;
        this.prefix = prefix;
    }

    @Override
    public long expireSeconds() {
        return this.expireSeconds;
    }

    @Override
    public String getPrefix() {
        return getClass().getSimpleName()+":"+prefix;
    }
}
