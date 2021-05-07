package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.vo.SeckillGoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2021-04-26
 */
public interface IGoodsService extends IService<Goods> {

    SeckillGoodsVo querySeckillGood(Long goodsId);
}
