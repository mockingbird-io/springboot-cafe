package com.mockingbird.Springbootcafe.service;

import com.mockingbird.Springbootcafe.dao.PropertyValueDAO;
import com.mockingbird.Springbootcafe.pojo.Product;
import com.mockingbird.Springbootcafe.pojo.Property;
import com.mockingbird.Springbootcafe.pojo.PropertyValue;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames="propertyValues")
public class PropertyValueService {
    @Resource
    PropertyValueDAO propertyValueDAO;
    @Resource
    PropertyService propertyService;

    @CacheEvict(allEntries=true)
    public void update(PropertyValue propertyValue) {
        propertyValueDAO.save(propertyValue);
    }

    @Cacheable(key="'propertyValues-pid-'+ #p0.id")
    public List<PropertyValue> list(Product product) {
        return propertyValueDAO.findByProductOrderByIdDesc(product);
    }

    public void init(Product product) {
        List<Property> propertyList = propertyService.listByCategory(product.getCategory());
        for (Property property : propertyList) {
            PropertyValue propertyValue = getByPropertyAndProduct(property, product);
            if (propertyValue == null) {
                propertyValue = new PropertyValue();
                propertyValue.setProperty(property);
                propertyValue.setProduct(product);
                propertyValueDAO.save(propertyValue);
            }
        }
    }

    @Cacheable(key="'propertyValues-one-pid-'+#p0.id+ '-ptid-' + #p1.id")
    public PropertyValue getByPropertyAndProduct(Property property, Product product) {
        return propertyValueDAO.getByPropertyAndProduct(property, product);
    }
}
