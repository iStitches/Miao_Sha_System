package com.xxxx.seckill.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xxxx.seckill.constant.JwtConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

// JWT工具类
@Component
@Slf4j
public class JWTUtil {
     private static String refreshTokenExpireTime;

     private static String encryptJwtKey;

     @Value("${jwt.refreshToken-expireTime}")
     public void setRefreshTokenExpireTime(String refreshTokenExpireTime) {
          JWTUtil.refreshTokenExpireTime = refreshTokenExpireTime;
     }

     @Value("${jwt.encrypt-jwtKey}")
     public void setEncryptJwtKey(String encryptJwtKey) {
          JWTUtil.encryptJwtKey = encryptJwtKey;
     }

     /**
      * 获取token中的信息
      * @param token
      * @param claim
      * @return
      */
     public String getClaim(String token, String claim){
          try {
               DecodedJWT decodedJWT = JWT.decode(token);
               return decodedJWT.getClaim(claim).toString();
          } catch (JWTDecodeException e) {
               log.error("解析token获取"+claim+"数据出现错误-----"+e.getMessage());
               e.printStackTrace();
          }
          return null;
     }

     /**
      * 检查token是否正确有效
      * @param token
      * @return
      */
     public boolean verify(String token){
          // 先生成账号+私钥
          try {
               String secret = getClaim(token,JwtConstant.ACCOUNT)+Base64Util.encode(encryptJwtKey);
               Algorithm algorithm = Algorithm.HMAC256(secret);
               JWTVerifier verifier = JWT.require(algorithm).build();
               DecodedJWT res = verifier.verify(token);
               return true;
          } catch (Exception e) {
               log.error("Token认证无效，请尝试重新登录---"+e.getMessage());
               e.printStackTrace();
          }
          return false;
     }

     /**
      * 生成token签名
      * @param account
      * @return
      */
     public static String sign(Long account, String currentTimeMillis){
          // 账号+私钥加密
          String secret = account+Base64Util.encode(encryptJwtKey);
          Algorithm algorithm = Algorithm.HMAC256(secret);
          Date expiretime = new Date(System.currentTimeMillis()+Long.parseLong(currentTimeMillis)*1000);
          return JWT.create().withClaim("account", account).withClaim("currentTimeMillis", currentTimeMillis)
                  .withExpiresAt(expiretime).sign(algorithm);
     }
}
