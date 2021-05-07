package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.seckill.pojo.*;
import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.redis.GoodsKey;
import com.xxxx.seckill.redis.MiaoShaKey;
import com.xxxx.seckill.redis.OrderKey;
import com.xxxx.seckill.redis.RedisService;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.vo.SeckillGoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-04-26
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    IGoodsService goodsService;
    @Autowired
    ISeckillGoodsService seckillGoodsService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    ISeckillOrderService seckillOrderService;
    @Autowired
    RedisService redisService;



    @Transactional(rollbackFor = {Exception.class})
    @Override
    public SeckillOrder doSeckill(Long id, SeckillGoodsVo goods) {
        //减库存、生成订单、秒杀订单
        try {
            //减数据库库存
            boolean success1 = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().eq("id",goods.getId()).set("stock_count",goods.getStockCount()-1));
            boolean success2 = goodsService.update(new UpdateWrapper<Goods>().eq("id",goods.getId()).set("goods_stock",goods.getGoodsStock()-1));

            //库存减少成功
            if(success1 && success2){
                //生成订单
                Order goodOrder = new Order();
                goodOrder.setGoodsId(goods.getId());
                goodOrder.setUserId(id);
                goodOrder.setGoodsName(goods.getGoodsName());
                goodOrder.setGoodsCount(1);
                goodOrder.setGoodsPrice(goods.getGoodsPrice());
                goodOrder.setCreateDate(new Date());
                Long orderId = orderMapper.insertOrder(goodOrder);
                //生成秒杀订单
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setGoodsId(goods.getId());
                seckillOrder.setUserId(id);
                seckillOrder.setOrderId(orderId);
                seckillOrderService.save(seckillOrder);
                //订单缓存
                redisService.set(OrderKey.getByUidGoodid,id+"_"+goods.getId(),seckillOrder);
                return seckillOrder;
            }

            //库存减少失败----说明库存已经为0，添加到缓存
            else{
                setGoodsOver(goods.getId());
                return null;
            }
        } catch (Exception e) {
            log.error("用户:{}秒杀--->商品{}失败",id,goods.getId());
            //秒杀失败，将减去的库存还原
            redisService.incr(GoodsKey.miaosha_goods_Stock,goods.getId()+"",1);
            //抛出异常进行回滚操作
            throw new RuntimeException();
        }
    }

    public void setGoodsOver(Long goodsId) {
        redisService.set(MiaoShaKey.isGoodsOver,goodsId,true);
    }

    public boolean getGoodsOver(Long goodsId) {
        return redisService.get(MiaoShaKey.isGoodsOver,goodsId+"",Boolean.class);
    }

    @Override
    public Long getMiaoShaResult(Long uid, Long gid) {
        SeckillOrder res = redisService.get(OrderKey.getByUidGoodid,uid+"_"+gid,SeckillOrder.class);
        if(res != null)
            return res.getOrderId();
        else{
            boolean isOver = getGoodsOver(gid);
            if(isOver)
                return -1L;
            else
                return 0L;
        }
    }


}
