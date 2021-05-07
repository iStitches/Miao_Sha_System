package com.xxxx.seckill.util;

import java.util.UUID;

public class SaltUtil {
    public static String getSalt(){
        String salt = UUID.randomUUID().toString().replace("-", "").substring(0,10);
        return salt;
    }

    public static void main(String[] args) {
        System.out.println(getSalt());
    }
}
