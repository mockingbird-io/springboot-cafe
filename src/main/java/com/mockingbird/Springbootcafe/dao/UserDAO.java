package com.mockingbird.Springbootcafe.dao;

import com.mockingbird.Springbootcafe.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<Users,Integer> {
    Users findByName(String name);
}
