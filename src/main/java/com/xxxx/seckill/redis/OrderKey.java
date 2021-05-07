package com.xxxx.seckill.redis;

public class OrderKey extends BasePrefix{
    OrderKey(long seconds, String prefix) {
        super(seconds, prefix);
    }

    OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getByUidGoodid = new OrderKey("moug");
}
