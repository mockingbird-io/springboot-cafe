package com.mockingbird.Springbootcafe.dao;
 
import org.springframework.data.jpa.repository.JpaRepository;

import com.mockingbird.Springbootcafe.pojo.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDAO extends JpaRepository<Category,Integer>{

}
