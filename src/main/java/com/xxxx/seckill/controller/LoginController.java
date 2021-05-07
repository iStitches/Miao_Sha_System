package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.seckill.constant.CommonEnum;
import com.xxxx.seckill.constant.JwtConstant;
import com.xxxx.seckill.constant.ResultObj;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.redis.RedisService;
import com.xxxx.seckill.redis.UserKey;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.util.JWTUtil;
import com.xxxx.seckill.util.Md5Util;
import com.xxxx.seckill.util.SaltUtil;
import com.xxxx.seckill.vo.RegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;


@RestController
@RequestMapping("/login")
public class LoginController {
    @Value("${jwt.refreshToken-expireTime}")
    private String refreshTokenExpireTime;
    @Value("${jwt.accessToken-expireTime}")
    private String accessTokenExpireTime;


    @Autowired
    RedisService redisService;
    @Autowired
    JWTUtil jwtUtil;
    @Autowired
    IUserService userService;

    /**
     * 用户登录操作：
     *    1.判断用户名和密码是否正确---数据库查询
     *    2.正确的话根据用户名清除Redis中缓存的信息(RefreshToken、用户实体信息user)
     *    3.生成jwt(accesstoken)、refreshtoken，将jwt放入到 Response响应头的header中返回
     * @param account
     * @param password
     * @param response
     * @return
     */
    @RequestMapping("/login")
    public ResultObj userLogin(@Validated @RequestParam(value = "account",required = true) Long account,
                               @RequestParam(value = "password",required = true) String password,
                               HttpServletResponse response){
        //数据库核验账号密码
        User dbRes = userService.getOne(new QueryWrapper<User>().eq("id",account));
        if(dbRes == null)
            return ResultObj.error(CommonEnum.ACCOUNT_NOT_FOUND);
        else if(!dbRes.getPassword().equals(Md5Util.formPassToDbPass(password,dbRes.getSalt())))
            return ResultObj.error(CommonEnum.PASSWORD_FALSE);

        //更新信息
        boolean isSuccess = userService.update(new UpdateWrapper<User>().eq("id", account).set("login_count", dbRes.getLoginCount() + 1).set(
                "last_login_date", new Date()));

        //个人信息缓存、refreshtoken缓存、jwt响应返回
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        redisService.set(UserKey.user_refreshToken,account,currentTimeMillis);
        redisService.set(UserKey.user_id,""+account,dbRes);
        String accessToken = JWTUtil.sign(account, currentTimeMillis);
        response.setHeader("Authorization",accessToken);
        response.setHeader("Access-Control-Expose-Headers","Authorization");
        return ResultObj.success();
    }

    //登出操作
    @RequestMapping("/logout")
    public ResultObj userLogout(HttpServletRequest request){
        String jwt = request.getHeader(JwtConstant.HEADER_AUTH);
        String account = jwtUtil.getClaim(jwt, JwtConstant.ACCOUNT);
        //1.清除Redis 缓存
        redisService.delete(UserKey.user_id,account);
        redisService.delete(UserKey.user_refreshToken,account);
        return ResultObj.success();
    }

    //注册操作
    @PostMapping("/register")
    public ResultObj userRegister(@Validated @RequestBody RegisterVo registerVo){
        String salt = SaltUtil.getSalt();
        LocalDateTime date = LocalDateTime.now();
        User dbRes = userService.getOne(new QueryWrapper<User>().eq("id",registerVo.getId()));
        if(dbRes != null)
            return ResultObj.error(CommonEnum.ACCOUNT_ALREADY_EXISTS);
        User user = new User(registerVo.getId(),registerVo.getNickname(),Md5Util.formPassToDbPass(registerVo.getPassword(), salt),salt,null,date,null,0);
        userService.save(user);
        return ResultObj.success("200","注册成功，请登录",null);
    }

    //方法压测
    @GetMapping("/info")
    public ResultObj userInfo(User user){
        System.out.println(user);
        return ResultObj.success(user);
    }
}
