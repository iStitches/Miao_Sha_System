package com.xxxx.seckill.redis;

public class MiaoShaKey extends BasePrefix{
    MiaoShaKey(long seconds, String prefix) {
        super(seconds, prefix);
    }

    MiaoShaKey(String prefix) {
        super(prefix);
    }

    public static MiaoShaKey isGoodsOver = new MiaoShaKey("go");
}
