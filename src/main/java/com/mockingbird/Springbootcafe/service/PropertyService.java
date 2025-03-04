package com.mockingbird.Springbootcafe.service;

import com.mockingbird.Springbootcafe.dao.PropertyDAO;
import com.mockingbird.Springbootcafe.pojo.Category;
import com.mockingbird.Springbootcafe.pojo.Property;
import com.mockingbird.Springbootcafe.util.Page4Navigator;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames="properties")
public class PropertyService {
    @Resource
    PropertyDAO propertyDAO;
    @Resource
    CategoryService categoryService;

    @CacheEvict(allEntries=true)
    public void add(Property bean) {
        propertyDAO.save(bean);
    }

    @CacheEvict(allEntries=true)
    public void delete(int id) {
        propertyDAO.deleteById(id);
    }

    @Cacheable(key="'properties-one-'+ #p0")
    public Property get(int id) {
        return propertyDAO.findById(id).orElse(null);
    }

    @CacheEvict(allEntries=true)
    public void update(Property bean) {
        propertyDAO.save(bean);
    }

    @Cacheable(key="'properties-cid-'+ #p0.id")
    public Page4Navigator<Property> list(int cid, int start, int size, int navigatePages) {
        Category category = categoryService.get(cid);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(start, size, sort);

        Page<Property> pageFromJPA = propertyDAO.findByCategory(category, pageable);

        return new Page4Navigator<>(pageFromJPA, navigatePages);

    }

    @Cacheable(key="'properties-cid-'+#p0+'-page-'+#p1 + '-' + #p2 ")
    public List<Property> listByCategory(Category category) {
        return propertyDAO.findByCategory(category);
    }

}
