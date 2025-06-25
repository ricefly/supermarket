package com.mysupermarket.supermarket.backend.service.manager;

import com.mysupermarket.supermarket.backend.entity.Order;
import com.mysupermarket.supermarket.backend.entity.OrderItem;
import com.mysupermarket.supermarket.backend.repository.OrderRepository;
import com.mysupermarket.supermarket.backend.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderManageService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    // 查询所有订单
    public List<Order> listOrders() {
        return orderRepository.findAll();
    }

    // 查询单个订单
    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }

    // 查询订单的所有订单项
    public List<OrderItem> getOrderItems(Integer orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
} 