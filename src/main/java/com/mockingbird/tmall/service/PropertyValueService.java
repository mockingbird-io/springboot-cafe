package com.mockingbird.tmall.service;

import com.mockingbird.tmall.dao.PropertyValueDAO;
import com.mockingbird.tmall.pojo.Product;
import com.mockingbird.tmall.pojo.Property;
import com.mockingbird.tmall.pojo.PropertyValue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PropertyValueService {
    @Resource
    PropertyValueDAO propertyValueDAO;
    @Resource
    PropertyService propertyService;

    public void update(PropertyValue propertyValue){
        propertyValueDAO.save(propertyValue);
    }

    public List<PropertyValue> list(Product product){
        return propertyValueDAO.findByProductOrderByIdDesc(product);
    }

    public void init(Product product){
        List<Property> propertyList = propertyService.listByCategory(product.getCategory());
        for (Property property : propertyList){
            PropertyValue propertyValue = getByPropertyAndProduct(property,product);
            if(propertyValue == null){
                propertyValue = new PropertyValue();
                propertyValue.setProperty(property);
                propertyValue.setProduct(product);
                propertyValueDAO.save(propertyValue);
            }
        }
    }

    public PropertyValue getByPropertyAndProduct(Property property, Product product){
        return propertyValueDAO.getByPropertyAndProduct(property,product);
    }
}
