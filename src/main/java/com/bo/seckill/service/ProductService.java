package com.bo.seckill.service;

/*
 **
 ** create by bo
 ** 2019/5/5
 */


public interface ProductService {

    String queryProduct(String productId);

    String seckill(String productId);

}
