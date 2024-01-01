package com.mockingbird.Springbootcafe.service;

import com.mockingbird.Springbootcafe.dao.ReviewDAO;
import com.mockingbird.Springbootcafe.pojo.Product;
import com.mockingbird.Springbootcafe.pojo.Review;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ReviewService {
    @Resource
    ReviewDAO reviewDAO;

    public void add(Review review) {
        reviewDAO.save(review);
    }

    public List<Review> list(Product product) {
        return reviewDAO.findByProductOrderByIdDesc(product);
    }

    public int getCount(Product product) {
        return reviewDAO.countByProduct(product);
    }
}
