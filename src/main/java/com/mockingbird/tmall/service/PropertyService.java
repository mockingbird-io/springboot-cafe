package com.mockingbird.tmall.service;

import com.mockingbird.tmall.dao.PropertyDAO;
import com.mockingbird.tmall.pojo.Category;
import com.mockingbird.tmall.pojo.Property;
import com.mockingbird.tmall.util.Page4Navigator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PropertyService {
    @Resource
    PropertyDAO propertyDAO;
    @Resource
    CategoryService categoryService;

    public void add(Property bean) {
        propertyDAO.save(bean);
    }

    public void delete(int id) {
        propertyDAO.deleteById(id);
    }

    public Property get(int id) {
        return propertyDAO.findById(id).orElse(null);
    }

    public void update(Property bean) {
        propertyDAO.save(bean);
    }

    public Page4Navigator<Property> list(int cid, int start, int size, int navigatePages){
        Category category = categoryService.get(cid);
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(start,size,sort);

        Page<Property> pageFromJPA = propertyDAO.findByCategory(category,pageable);

        return new Page4Navigator<>(pageFromJPA,navigatePages);

    }

    public List<Property> listByCategory(Category category){
        return propertyDAO.findByCategory(category);
    }

}
