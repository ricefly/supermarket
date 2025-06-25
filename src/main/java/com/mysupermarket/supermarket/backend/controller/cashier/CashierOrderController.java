package com.mysupermarket.supermarket.backend.controller.cashier;

import com.mysupermarket.supermarket.backend.dto.OrderSettleRequest;
import com.mysupermarket.supermarket.backend.service.cashier.CashierOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cashier/orders")
public class CashierOrderController {
    @Autowired
    private CashierOrderService cashierOrderService;

    @PostMapping("/settle")
    public Map<String, Object> settleOrder(@RequestBody OrderSettleRequest request) {
        Integer orderId = cashierOrderService.settleOrder(request);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("orderId", orderId);
        return result;
    }
} 