package com.xxxx.seckill.redis;

//Redis存储用户信息键值对
public class UserKey extends BasePrefix{
    private static final long expireTime = 1800;

    UserKey(String prefix) {
        super(prefix);
    }
    UserKey(long expireTime, String prefix){
        super(expireTime,prefix);
    }

    //UserKey:id1
    public static UserKey user_id = new UserKey("id");
    //UserKey:namexxx
    public static UserKey user_name = new UserKey("name");
    //UserKey:refreshTokenxxxx
    public static UserKey user_refreshToken = new UserKey(expireTime,"refreshToken");
}
