package com.mockingbird.Springbootcafe.dao;

import com.mockingbird.Springbootcafe.pojo.Category;
import com.mockingbird.Springbootcafe.pojo.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyDAO extends JpaRepository<Property,Integer> {
    Page<Property> findByCategory(Category category, Pageable pageable);
    List<Property> findByCategory(Category category);

}
