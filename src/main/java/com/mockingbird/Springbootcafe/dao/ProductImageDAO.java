package com.mockingbird.Springbootcafe.dao;

import com.mockingbird.Springbootcafe.pojo.Product;
import com.mockingbird.Springbootcafe.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageDAO extends JpaRepository<ProductImage,Integer> {
    List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);
}
