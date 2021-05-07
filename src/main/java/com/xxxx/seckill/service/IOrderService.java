package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.SeckillGoods;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.SeckillGoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2021-04-26
 */
public interface IOrderService extends IService<Order> {
     SeckillOrder doSeckill(Long id, SeckillGoodsVo goods);

     Long getMiaoShaResult(Long uid, Long gid);
}
