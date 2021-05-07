package com.xxxx.seckill.rabbitmq;

import com.xxxx.seckill.util.JSONUtil;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MiaoShaSender {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //消息加入消息队列
    public boolean addMiaoShaQueue(MiaoShaMessage message){
        try {
            String msg = JSONUtil.beanToString(message);
            rabbitTemplate.convertAndSend(MQConfig.MIAOSHA_EXCHANGE,MQConfig.MIAOSHA_ROUTING_KEY,msg);
            return true;
        } catch (AmqpException e) {
            e.printStackTrace();
            return false;
        }
    }
}
