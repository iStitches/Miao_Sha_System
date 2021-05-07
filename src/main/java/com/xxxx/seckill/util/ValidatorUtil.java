package com.xxxx.seckill.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义校验工具类
 */
public class ValidatorUtil {

    //合格手机格式
    private static final Pattern phone_pattern = Pattern.compile("[1][3-9][0-9]{9}");

    public static boolean isMobile(Long phone){
        if(phone == null)
            return false;
        String phstr = String.valueOf(phone);
        Matcher matcher = phone_pattern.matcher(phstr);
        return matcher.matches();
    }
}
