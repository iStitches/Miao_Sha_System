package com.xxxx.seckill.config;

import com.xxxx.seckill.constant.JwtConstant;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.redis.RedisService;
import com.xxxx.seckill.redis.UserKey;
import com.xxxx.seckill.util.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;

@Configuration
public class MyHandlerArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    RedisService  redisService;
    @Autowired
    JWTUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String token = request.getHeader(JwtConstant.HEADER_AUTH);

        if(StringUtils.isBlank(token)){
            return null;
        }
        if(!jwtUtil.verify(token)){
            return null;
        }
        String account = jwtUtil.getClaim(token,JwtConstant.ACCOUNT);
        User user = null;
        if(StringUtils.isNotBlank(account)){
            user = redisService.get(UserKey.user_id,account,User.class);
        }
        return user;
    }
}
