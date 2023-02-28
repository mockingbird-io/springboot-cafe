package com.mockingbird.Springbootcafe.dao;

import com.mockingbird.Springbootcafe.pojo.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDAO extends JpaRepository<Order,Integer> {
}
