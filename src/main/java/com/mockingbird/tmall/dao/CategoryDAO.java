package com.mockingbird.tmall.dao;
 
import org.springframework.data.jpa.repository.JpaRepository;

import com.mockingbird.tmall.pojo.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDAO extends JpaRepository<Category,Integer>{

}
