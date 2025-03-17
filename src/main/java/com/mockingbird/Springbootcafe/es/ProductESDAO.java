package com.mockingbird.Springbootcafe.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.mockingbird.Springbootcafe.pojo.Product;

public interface ProductESDAO extends ElasticsearchRepository<Product,Integer>{
    // 添加按名称查询的方法
    Page<Product> findByNameContaining(String name, Pageable pageable);

}