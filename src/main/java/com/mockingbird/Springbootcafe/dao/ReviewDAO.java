package com.mockingbird.Springbootcafe.dao;

import com.mockingbird.Springbootcafe.pojo.Product;
import com.mockingbird.Springbootcafe.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewDAO extends JpaRepository<Review,Integer> {
    List<Review> findByProductOrderByIdDesc(Product product);
    int countByProduct(Product product);
}
