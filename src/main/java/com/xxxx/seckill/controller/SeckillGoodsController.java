package com.xxxx.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.xxxx.seckill.constant.CommonEnum;
import com.xxxx.seckill.constant.ResultObj;
import com.xxxx.seckill.pojo.SeckillGoods;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MiaoShaMessage;
import com.xxxx.seckill.rabbitmq.MiaoShaSender;
import com.xxxx.seckill.redis.GoodsKey;
import com.xxxx.seckill.redis.RedisService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.vo.SeckillGoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2021-04-26
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController implements InitializingBean {
    @Autowired
    ISeckillGoodsService seckillGoodsService;
    @Autowired
    ISeckillOrderService seckillOrderService;
    @Autowired
    IOrderService orderService;
    @Autowired
    RedisService redisService;
    @Autowired
    MiaoShaSender sender;

    //内存存储库存为0的商品信息
    private HashMap<Long, Boolean> goodsOverMap = new HashMap<Long, Boolean>();

    /**
     * 初始化参数设置：  将某种商品的库存容量预先加载到Redis缓存中，通过缓存预减来减少不必要的数据库访问
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<SeckillGoods> list = seckillGoodsService.list();
        for(SeckillGoods goods: list){
            redisService.set(GoodsKey.miaosha_goods_Stock,goods.getGoodsId(),goods.getStockCount());
            if(goods.getStockCount() <= 0)
                goodsOverMap.put(goods.getGoodsId(),true);
            else
                goodsOverMap.put(goods.getGoodsId(),false);
        }
    }

    //展示所有秒杀商品信息
    @GetMapping("/list")
    public ResultObj getGoodsList(){
        List<SeckillGoodsVo> res = seckillGoodsService.getAll();
        System.out.println(res);
        return ResultObj.success(res);
    }

    //秒杀商品
    @GetMapping("/doSeckill")
        public ResultObj robGoods(User user,
                                  @RequestParam Long goodsId){
         if(user == null)
            return ResultObj.error(CommonEnum.ACCOUNT_VERIFY_FAIL);
        //先判断是否重复抢购---查询缓存中的订单
        SeckillOrder historyOrder = seckillOrderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(historyOrder != null)
            return ResultObj.error(CommonEnum.GOODS_ALREADY_ROB);

         //根据内存标记的已清仓产品，减少redis访问
         boolean flag = goodsOverMap.get(goodsId);
         if(flag)
            return ResultObj.error(CommonEnum.GOODS_STACK_ZERO);

        //redis预减库存
        long line = redisService.decr(GoodsKey.miaosha_goods_Stock,""+goodsId,1);
        if(line < 0) {
            goodsOverMap.put(goodsId,true);
            return ResultObj.error(CommonEnum.MIAO_SHA_ERROR);
        }

        //可以秒杀，进入消息队列排队
        MiaoShaMessage message = new MiaoShaMessage(user,goodsId);
        sender.addMiaoShaQueue(message);
        return ResultObj.success("200","等待排队中...",null);
    }

    /**
     * 获取秒杀结果
     * 0：排队中
     * -1：秒杀失败
     * 其它：秒杀订单号
     * @param user
     * @param gid
     * @return
     */
    @GetMapping("/result")
    public ResultObj getMiaoShaResult(User user,
                                      @RequestParam Long gid){
        if(user == null)
            return ResultObj.error(CommonEnum.ACCOUNT_VERIFY_FAIL);
        Long result = orderService.getMiaoShaResult(user.getId(),gid);
        return ResultObj.success(result);
    }
}
