package com.bo.seckill.repository;

/*
 **
 ** create by bo
 ** 2019/5/5
 */

import com.bo.seckill.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductJPARepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor {


}
