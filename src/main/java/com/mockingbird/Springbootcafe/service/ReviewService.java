package com.mockingbird.Springbootcafe.service;

import com.mockingbird.Springbootcafe.dao.ReviewDAO;
import com.mockingbird.Springbootcafe.pojo.Product;
import com.mockingbird.Springbootcafe.pojo.Review;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = "reviews")
public class ReviewService {
    @Resource
    ReviewDAO reviewDAO;

    @CacheEvict(allEntries = true)
    public void add(Review review) {
        reviewDAO.save(review);
    }

    @Cacheable(key="'reviews-pid-'+ #p0.id")
    public List<Review> list(Product product) {
        return reviewDAO.findByProductOrderByIdDesc(product);
    }

    @Cacheable(key="'reviews-count-pid-'+ #p0.id")
    public int getCount(Product product) {
        return reviewDAO.countByProduct(product);
    }
}
