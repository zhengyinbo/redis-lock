package com.bo.seckill.service.impl;

/*
 **
 ** create by bo
 ** 2019/5/6
 */

import com.bo.seckill.service.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class RedisLockServiceImpl implements RedisLockService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     * @param key productId
     * @param value 当前时间 + 超时时间
     * @return
     */
    @Override
    public Boolean lock(String key, String value) {
        //加锁成功
        if (redisTemplate.opsForValue().setIfAbsent(key, value)){
            return true;
        }
        String currentValue = redisTemplate.opsForValue().get(key);
        //锁过期  避免造成死锁
        if (!StringUtils.isEmpty(currentValue) &&
                Long.parseLong(currentValue) < System.currentTimeMillis()){
            //获取上一个锁的时间
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)){
                return true;
            }
        }
        //加锁失败
        return false;
    }

    /**
     * 解锁
     * @param key
     * @param value
     * @return
     */
    @Override
    public Boolean unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)){
                redisTemplate.opsForValue().getOperations().delete(key);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("[redis分布式锁] 解锁异常");
        }
        return false;
    }

}
