package com.xxxx.seckill.config;

import com.xxxx.seckill.constant.CommonEnum;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//自定义拦截器
@Configuration
public class MyInterceptor implements HandlerInterceptor {
    @Autowired
    JWTUtil jwtUtil;

    //Controller方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwtToken = request.getHeader("Authorization");
        boolean verify = jwtUtil.verify(jwtToken);
        if(verify){
           return true;
        }
        return false;
    }

    //Controller方法执行之后，视图渲染之前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //方法执行完之后，常用于清理资源
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
