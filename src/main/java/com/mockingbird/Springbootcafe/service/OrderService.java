package com.mockingbird.Springbootcafe.service;

import com.mockingbird.Springbootcafe.dao.OrderDAO;
import com.mockingbird.Springbootcafe.pojo.Order;
import com.mockingbird.Springbootcafe.pojo.OrderItem;
import com.mockingbird.Springbootcafe.pojo.Users;
import com.mockingbird.Springbootcafe.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderService {
    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitConfirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    public static final String delete = "delete";

    @Resource
    OrderDAO orderDAO;
    @Autowired
    private OrderItemService orderItemService;

    public Page4Navigator<Order> list(int start, int size, int navigatePages) {
        Pageable pageable = PageRequest.of(start, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Order> pageFromJPA = orderDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA, navigatePages);
    }

    public Order get(int id) {
        return orderDAO.findById(id).orElse(null);
    }

    public void update(Order order) {
        orderDAO.save(order);
    }

    public void removeOrderFromOrderItem(List<Order> orders) {
        for (Order order : orders) {
            removeOrderFromOrderItem(order);
        }
    }

    private void removeOrderFromOrderItem(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(null);
        }
    }

    public void add(Order order) {
        orderDAO.save(order);
    }

    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public float add(Order order, List<OrderItem> ois) {
        float total = 0;
        add(order);

        if(false)
            throw new RuntimeException();

        for (OrderItem oi: ois) {
            oi.setOrder(order);
            orderItemService.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return total;
    }

    public List<Order> listByUserWithoutDelete(Users user) {
        List<Order> orders = listByUserAndNotDeleted(user);
        orderItemService.fill(orders);
        return orders;
    }

    public List<Order> listByUserAndNotDeleted(Users user) {
        return orderDAO.findByUsersAndStatusNotOrderByIdDesc(user, OrderService.delete);
    }


}
