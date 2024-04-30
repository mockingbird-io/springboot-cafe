package com.mockingbird.Springbootcafe.dao;

import com.mockingbird.Springbootcafe.pojo.Order;
import com.mockingbird.Springbootcafe.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDAO extends JpaRepository<Order,Integer> {
    List<Order> findByUsersAndStatusNotOrderByIdDesc(Users user, String status);
}
