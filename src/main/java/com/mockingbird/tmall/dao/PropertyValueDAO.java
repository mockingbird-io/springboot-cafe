package com.mockingbird.tmall.dao;

import com.mockingbird.tmall.pojo.Product;
import com.mockingbird.tmall.pojo.Property;
import com.mockingbird.tmall.pojo.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyValueDAO extends JpaRepository<PropertyValue,Integer> {
    List<PropertyValue> findByProductOrderByIdDesc(Product product);
    PropertyValue getByPropertyAndProduct(Property property, Product product);
}
