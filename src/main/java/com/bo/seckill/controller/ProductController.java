package com.bo.seckill.controller;

/*
 **
 ** create by bo
 ** 2019/5/5
 */

import com.bo.seckill.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/query")
    public String query(String productId) throws Exception{
        return productService.queryProduct(productId);
    }

    @GetMapping("/order")
    public String seckill(String productId) throws Exception{
        log.info("productId: " + productId);
        productService.seckill(productId);
        return productService.queryProduct(productId);
    }

}
