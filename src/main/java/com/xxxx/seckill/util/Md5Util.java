package com.xxxx.seckill.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * md5 加密工具类
 */
public class Md5Util {
    private static final String salt = "1a2b3c4d";

    private static String md5(String t){
        return DigestUtils.md5Hex(t);
    }

    //从用户输入密码加密为传输密码
    public static String inputPassToFormPass(String pass){
        String res = ""+salt.charAt(0)+salt.charAt(4)+pass+salt.charAt(2)+salt.charAt(1)+salt.charAt(3);
        return md5(res);
    }

    //从传输密码加密为数据库存储密码
    public static String formPassToDbPass(String pass,String salt){
        String s = ""+salt.charAt(3)+salt.charAt(1)+salt.charAt(5)+salt.charAt(4)+pass+salt.charAt(6)+salt.charAt(7);
        return md5(s);
    }

    //从用户输入加密到数据库存储
    public static String inputToDbPass(String pass,String salt){
        String form = inputPassToFormPass(pass);
        String db = formPassToDbPass(form,salt);
        return db;
    }

    public static void main(String[] args) {
            System.out.println(inputToDbPass("18887654321","1a2b3c4d5e"));
    }
}

//c2e54631347396cee0a4e346d29abd92
