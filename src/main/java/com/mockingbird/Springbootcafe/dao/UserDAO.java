package com.mockingbird.Springbootcafe.dao;

import com.mockingbird.Springbootcafe.pojo.Users;
import org.hsqldb.rights.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<Users,Integer> {
    Users findByName(String name);

    Users getByNameAndPassword(String name, String password);
}
