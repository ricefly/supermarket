package com.mysupermarket.supermarket.backend.controller.manager;

import com.mysupermarket.supermarket.backend.entity.Order;
import com.mysupermarket.supermarket.backend.entity.OrderItem;
import com.mysupermarket.supermarket.backend.service.manager.OrderManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/manager/orders")
public class OrderManageController {
    @Autowired
    private OrderManageService orderManageService;

    // 查询所有订单
    @GetMapping
    public List<Order> list() {
        return orderManageService.listOrders();
    }

    // 查询单个订单
    @GetMapping("/{id}")
    public Optional<Order> get(@PathVariable Integer id) {
        return orderManageService.getOrderById(id);
    }

    // 查询订单的所有订单项
    @GetMapping("/{id}/items")
    public List<OrderItem> getItems(@PathVariable Integer id) {
        return orderManageService.getOrderItems(id);
    }
} 