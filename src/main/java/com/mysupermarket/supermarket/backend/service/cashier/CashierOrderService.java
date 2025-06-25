package com.mysupermarket.supermarket.backend.service.cashier;

import com.mysupermarket.supermarket.backend.dto.OrderSettleRequest;
import com.mysupermarket.supermarket.backend.entity.Order;
import com.mysupermarket.supermarket.backend.entity.OrderItem;
import com.mysupermarket.supermarket.backend.repository.OrderRepository;
import com.mysupermarket.supermarket.backend.repository.OrderItemRepository;
import com.mysupermarket.supermarket.backend.repository.MemberRepository;
import com.mysupermarket.supermarket.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CashierOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Integer settleOrder(OrderSettleRequest request) {
        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderSettleRequest.OrderItemDTO item : request.getItems()) {
            totalAmount = totalAmount.add(item.getItemTotal());
        }
        // 创建订单
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        // 会员手机号转ID
        if (request.getMemberPhone() != null && !request.getMemberPhone().isEmpty()) {
            var member = memberRepository.findByPhoneNumber(request.getMemberPhone());
            if (member != null) {
                order.setMemberId(member.getMemberId());
                // 累加返利金额到会员账户
                BigDecimal rebate = totalAmount.multiply(new BigDecimal("0.01"));
                if (member.getRebateBalance() == null) member.setRebateBalance(BigDecimal.ZERO);
                member.setRebateBalance(member.getRebateBalance().add(rebate));
                memberRepository.save(member);
            } else {
                throw new RuntimeException("找不到该会员");
            }
        } else {
            order.setMemberId(null);
        }
        order.setCashierEmployeeId(request.getCashierEmployeeId());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setOrderStatus("已完成");
        order.setRebateEarned(totalAmount.multiply(new BigDecimal("0.01")));
        // 保存订单
        order = orderRepository.save(order);
        // 保存订单项
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderSettleRequest.OrderItemDTO itemDTO : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setOrderId(order.getOrderId());
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setItemTotal(itemDTO.getItemTotal());
            orderItems.add(item);
            // 扣减商品库存
            var productOpt = productRepository.findById(itemDTO.getProductId());
            if (productOpt.isPresent()) {
                var product = productOpt.get();
                int newStock = product.getCurrentStock() - itemDTO.getQuantity();
                product.setCurrentStock(Math.max(newStock, 0));
                productRepository.save(product);
            }
        }
        orderItemRepository.saveAll(orderItems);
        return order.getOrderId();
    }
} 