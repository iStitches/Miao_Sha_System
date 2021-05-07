package com.xxxx.seckill.controller;


import com.xxxx.seckill.constant.ResultObj;
import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.redis.GoodsKey;
import com.xxxx.seckill.redis.RedisService;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    RedisService redisService;
    @Autowired
    IGoodsService goodsService;

    /**
     * 商品列表
     * @return
     */
    @GetMapping("/list")
    public ResultObj getGoodsList(){
        //先从缓存中获取
        ArrayList<Goods> res = redisService.get(GoodsKey.goods_List, "", ArrayList.class);
        if(res!=null && res.size()>0)
        {
            return ResultObj.success(res);
        }
        //查不到从数据库查询并更新
        else {
            List<Goods> list = goodsService.list();
            redisService.set(GoodsKey.goods_List,"",list);
            return ResultObj.success(list);
        }
    }

    @GetMapping("/detail/{goodsId}")
    public ResultObj getDetailGoods(@PathVariable Long goodsId){
        //先从缓存中获取
        Goods good = redisService.get(GoodsKey.goods_Detail, "" + goodsId, Goods.class);
        if(good!=null)
            return ResultObj.success(good);
        //从数据库获取并设置进缓存
        else {
            Goods goods = goodsService.getById(goodsId);
            redisService.set(GoodsKey.goods_Detail,goodsId,goods);
            return ResultObj.success(goods);
        }
    }


}
