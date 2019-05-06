package com.bo.seckill.service.impl;

/*
 ** redis加锁与解锁
 ** create by bo
 ** 2019/5/5
 */

import com.bo.seckill.service.ProductService;
import com.bo.seckill.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisLockService redisLockService;

    private static final int TIMEOUT = 10 * 1000;  //超时时间

    static Map<String, Integer> products;
    static Map<String, Integer> stock;
    static Map<String, String> orders;
    static {
        products = new HashMap<>();
        stock = new HashMap<>();
        orders = new HashMap<>();
        products.put("123456", 100000);
        stock.put("123456", 100000);
    }

    @Override
    public String queryProduct(String productId) {
//        redisTemplate.opsForValue().setIfAbsent(productId, "A");
//        System.out.println(redisTemplate.opsForValue().get(productId));
//        System.out.println(redisTemplate.opsForValue().setIfAbsent(productId, "C"));//
//        redisTemplate.opsForValue().getOperations().delete(productId);
//        System.out.println(redisTemplate.opsForValue().get(productId));
        return "还剩："+ stock.get(productId) + "  份，" +
                "该商品成功下单用户数目：" + orders.size() + "人";
    }

    @Override
    public synchronized String seckill(String productId) {

        //加锁
        long time = System.currentTimeMillis() + TIMEOUT;
        if (!redisLockService.lock(productId, String.valueOf(time))){
            return "人太多了，亲稍后再试";
        }

        int stockNum = stock.get(productId);
        if (stockNum == 0){
            return "活动结束";
        }else {
            orders.put(String.valueOf((long)(Math.random()*1000000000)), productId);
            stockNum  = stockNum - 1;
            try {
                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }
            stock.put(productId, stockNum);
        }

        //解锁
        if (!redisLockService.unlock(productId, String.valueOf(time))){
            return "redis 解锁异常";
        }

        System.out.println("秒杀成功!");
        return "秒杀成功!";
    }

}
