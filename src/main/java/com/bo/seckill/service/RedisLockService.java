package com.bo.seckill.service;

/*
 ** redis锁
 ** create by bo
 ** 2019/5/6
 */

public interface RedisLockService {

    Boolean lock(String key, String value);

    Boolean unlock(String key, String value);

}
