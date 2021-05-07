package com.xxxx.seckill.rabbitmq;

import com.xxxx.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiaoShaMessage {
    private User user;
    private Long goodsId;
}
