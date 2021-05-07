package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2021-04-26
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    SeckillOrder getMiaoshaOrderByUserIdGoodsId(Long id, Long goodsId);
}
