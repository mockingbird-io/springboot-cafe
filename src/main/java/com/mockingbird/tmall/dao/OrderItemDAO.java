package com.mockingbird.tmall.dao;

import com.mockingbird.tmall.pojo.Order;
import com.mockingbird.tmall.pojo.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemDAO extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByOrderOrderByIdDesc(Order order);
}
