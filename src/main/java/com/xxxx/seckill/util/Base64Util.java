package com.xxxx.seckill.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 *  Base64加密解密工具类
 */
public class Base64Util {
    /**
     * 字符串加密
     * @param str
     * @return
     */
    public static String encode(String str){
        try {
            byte[] res = Base64.getEncoder().encode(str.getBytes("utf-8"));
            return new String(res);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字节码加密
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes){
         byte[] res = Base64.getEncoder().encode(bytes);
         return new String(res);
    }

    /**
     * 字符串解密
     * @param str
     * @return
     */
    public static String decode(String str){
        try {
            byte[] res = Base64.getDecoder().decode(str.getBytes("utf-8"));
            return new String(res);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
