package com.xxxx.seckill.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    //队列
    public static final String MIAOSHA_QUEUE = "miaosha.queue";
    //交换机
    public static final String MIAOSHA_EXCHANGE = "miaosha.exchange";
    //路由键
    public static final String MIAOSHA_ROUTING_KEY = "miaosha.routingKey";

    //配置交换机---直连
    @Bean
    public DirectExchange getMiaoShaExchange(){
        return new DirectExchange(MIAOSHA_EXCHANGE);
    }
    //配置队列---直连
    @Bean
    public Queue getMiaoShaQueue(){
        return new Queue(MIAOSHA_QUEUE);
    }
    //配置绑定
    @Bean
    public Binding miaoShaBinding(){
        return BindingBuilder.bind(getMiaoShaQueue()).to(getMiaoShaExchange()).with(MIAOSHA_ROUTING_KEY);
    }
}
