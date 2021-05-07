package com.xxxx.seckill.service.impl;

import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.mapper.SeckillOrderMapper;
import com.xxxx.seckill.redis.OrderKey;
import com.xxxx.seckill.redis.RedisService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-04-26
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    RedisService redisService;

    @Override
    public SeckillOrder getMiaoshaOrderByUserIdGoodsId(Long id, Long goodsId) {
       return redisService.get(OrderKey.getByUidGoodid,id+"_"+goodsId,SeckillOrder.class);
    }
}
