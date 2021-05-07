package com.xxxx.seckill.vo;

import com.xxxx.seckill.validation.isMobile;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RegisterVo {
    @isMobile
    private Long id;

    @NotNull(message = "昵称不能为空")
    @Size(min = 4,max = 8,message = "昵称大于4位小于8位")
    private String nickname;

    @Size(min = 8,message = "密码最少为8位")
    private String password;
}
