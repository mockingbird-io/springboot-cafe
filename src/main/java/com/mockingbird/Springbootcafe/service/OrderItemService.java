package com.mockingbird.Springbootcafe.service;

import com.mockingbird.Springbootcafe.dao.OrderItemDAO;
import com.mockingbird.Springbootcafe.pojo.Order;
import com.mockingbird.Springbootcafe.pojo.OrderItem;
import com.mockingbird.Springbootcafe.pojo.Product;
import com.mockingbird.Springbootcafe.pojo.Users;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames="orderItems")
public class OrderItemService {
    @Resource
    OrderItemDAO orderItemDAO;
    @Resource
    ProductImageService productImageService;

    @CacheEvict(allEntries=true)
    public void update(OrderItem orderItem) {
        orderItemDAO.save(orderItem);
    }

    public void fill(List<Order> orders) {
        for (Order order : orders) {
            fill(order);
        }
    }

    public void fill(Order order) {
        List<OrderItem> list = listByOrder(order);
        float total = 0;
        int totalNumber = 0;
        for (OrderItem orderItem : list) {
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            totalNumber += orderItem.getNumber();
            productImageService.setFirstProductImage(orderItem.getProduct());
        }
        order.setTotal(total);
        order.setTotalNumber(totalNumber);
        order.setOrderItems(list);
    }

    @Cacheable(key="'orderItems-oid-'+ #p0.id")
    public List<OrderItem> listByOrder(Order order) {
        return orderItemDAO.findByOrderOrderByIdDesc(order);
    }

    @CacheEvict(allEntries=true)
    public void add(OrderItem orderItem) {
        orderItemDAO.save(orderItem);
    }

    @Cacheable(key="'orderItems-one-'+ #p0")
    public OrderItem get(int id) {
        return orderItemDAO.findById(id).orElse(null);
    }

    @CacheEvict(allEntries=true)
    public void delete(int id) {
        orderItemDAO.deleteById(id);
    }

    public int getSaleCount(Product product) {
        List<OrderItem> ois =listByProduct(product);
        int result =0;


        for (OrderItem oi : ois) {
            if(null!=oi.getOrder())
                if(null != oi.getOrder().getPayDate())
                    result+=oi.getNumber();
        }
        return result;
    }

    @Cacheable(key="'orderItems-pid-'+ #p0.id")
    public List<OrderItem> listByProduct(Product product) {
        return orderItemDAO.findByProduct(product);
    }


    public List<OrderItem> listByUsers(Users users){
        return orderItemDAO.findByUsersAndOrderIsNull(users);
    }

    @Cacheable(key="'orderItems-uid-'+ #p0.id")
    public List<OrderItem> listByUser(Users user) {
        return orderItemDAO.findByUsersAndOrderIsNull(user);
    }
}
