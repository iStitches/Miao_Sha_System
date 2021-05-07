package com.xxxx.seckill.service.impl;

import com.xxxx.seckill.pojo.SeckillGoods;
import com.xxxx.seckill.mapper.SeckillGoodsMapper;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.vo.SeckillGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2021-04-26
 */
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements ISeckillGoodsService {
    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;

    @Override
    public List<SeckillGoodsVo> getAll() {
       return seckillGoodsMapper.listAllGoods();
    }

    @Override
    public SeckillGoodsVo getInfoByGoodId(Long goodId) {
        return seckillGoodsMapper.getInfoByGoodId(goodId);
    }
}
