package com.xxxx.seckill.mapper;

import com.xxxx.seckill.pojo.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2021-04-26
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
     Long insertOrder(Order order);
}
