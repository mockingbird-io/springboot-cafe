package com.mockingbird.tmall.web;

import com.mockingbird.tmall.pojo.Order;
import com.mockingbird.tmall.service.OrderItemService;
import com.mockingbird.tmall.service.OrderService;
import com.mockingbird.tmall.util.Page4Navigator;
import com.mockingbird.tmall.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

@RestController
public class OrderController {
    @Resource
    OrderService orderService;
    @Resource
    OrderItemService orderItemService;

    @GetMapping("/orders")
    public Page4Navigator<Order> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        Page4Navigator<Order> page =orderService.list(start, size, 5);
        orderItemService.fill(page.getContent());
        orderService.removeOrderFromOrderItem(page.getContent());
        return page;
    }
    @PutMapping("deliveryOrder/{oid}")
    public Object deliveryOrder(@PathVariable("oid") int oid) throws IOException {
        Order o = orderService.get(oid);
        o.setDeliveryDate(new Date());
        o.setStatus(OrderService.waitConfirm);
        orderService.update(o);
        return Result.success();
    }
}
