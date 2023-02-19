package com.mockingbird.tmall.service;

import com.mockingbird.tmall.dao.OrderItemDAO;
import com.mockingbird.tmall.pojo.Order;
import com.mockingbird.tmall.pojo.OrderItem;
import com.mockingbird.tmall.pojo.Product;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderItemService {
    @Resource
    OrderItemDAO orderItemDAO;
    @Resource
    ProductImageService productImageService;

    public void fill(List<Order> orders){
        for (Order order : orders){
            fill(order);
        }
    }

    public void fill(Order order){
        List<OrderItem> list = listByOrder(order);
        float total = 0;
        int totalNumber = 0;
        for (OrderItem orderItem : list){
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            totalNumber += orderItem.getNumber();
            productImageService.setFirstProductImage(orderItem.getProduct());
        }
        order.setTotal(total);
        order.setTotalNumber(totalNumber);
        order.setOrderItems(list);
    }
    public List<OrderItem> listByOrder(Order order) {
        return orderItemDAO.findByOrderOrderByIdDesc(order);
    }

}
