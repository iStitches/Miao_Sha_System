package com.xxxx.seckill.mapper;

import com.xxxx.seckill.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.seckill.pojo.SeckillGoods;
import com.xxxx.seckill.vo.SeckillGoodsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2021-04-26
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
     SeckillGoodsVo findByGoodId(Long goodId);
}
