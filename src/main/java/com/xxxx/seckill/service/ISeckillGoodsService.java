package com.xxxx.seckill.service;

import com.xxxx.seckill.pojo.SeckillGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.vo.SeckillGoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2021-04-26
 */
public interface ISeckillGoodsService extends IService<SeckillGoods> {
    List<SeckillGoodsVo> getAll();

    SeckillGoodsVo getInfoByGoodId(Long goodId);
}
