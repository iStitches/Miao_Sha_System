package com.xxxx.seckill.service.impl;

import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.mapper.GoodsMapper;
import com.xxxx.seckill.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.vo.SeckillGoodsVo;
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
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Autowired
    GoodsMapper goodsMapper;


    @Override
    public SeckillGoodsVo querySeckillGood(Long goodsId) {
        return goodsMapper.findByGoodId(goodsId);
    }
}
