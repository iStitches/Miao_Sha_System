package com.xxxx.seckill.rabbitmq;

import com.xxxx.seckill.constant.CommonEnum;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.redis.OrderKey;
import com.xxxx.seckill.redis.RedisService;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.util.JSONUtil;
import com.xxxx.seckill.vo.SeckillGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MiaoShaReceiver {
    @Autowired
    IGoodsService goodsService;
    @Autowired
    IOrderService orderService;
    @Autowired
    ISeckillOrderService seckillOrderService;
    @Autowired
    RedisService redisService;


    @RabbitListener(queues = {MQConfig.MIAOSHA_QUEUE})
    public void receive(String msg){
        //从队列中拿消息
        MiaoShaMessage miaoShaMessage = JSONUtil.stringToBean(msg, MiaoShaMessage.class);
        if(miaoShaMessage == null)
            throw new RuntimeException("消息队列消费者接收消息异常");
        User user = miaoShaMessage.getUser();
        Long goodsId = miaoShaMessage.getGoodsId();

        //真实库存判断
        SeckillGoodsVo goodsVo = goodsService.querySeckillGood(goodsId);
        if(goodsVo.getStockCount() <= 0)
            throw new GlobalException(CommonEnum.GOODS_STACK_ZERO);

        //再次判断用户是否重复秒杀
        SeckillOrder order = redisService.get(OrderKey.getByUidGoodid,user.getId()+"_"+goodsId, SeckillOrder.class);
        if(order != null)
            throw new GlobalException(CommonEnum.GOODS_ALREADY_ROB);

        //秒杀减库存生成订单
        try {
            orderService.doSeckill(user.getId(),goodsVo);
        } catch (Exception e) {
            log.error("消息接收处理时发现异常：{}"+e.getMessage());
        }
    }
}
