package com.xxxx.seckill.redis;

public class GoodsKey extends BasePrefix{
    GoodsKey(long seconds, String prefix) {
        super(seconds, prefix);
    }

    public static GoodsKey goods_List = new GoodsKey(60,"gl");
    public static GoodsKey goods_Detail = new GoodsKey(60,"gd");
    public static GoodsKey miaosha_goods_Stock = new GoodsKey(0,"gs");
}
