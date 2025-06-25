package com.mysupermarket.supermarket.backend.controller.warehouse;

import com.mysupermarket.supermarket.backend.entity.ProductLossRecord;
import com.mysupermarket.supermarket.backend.service.warehouse.WarehouseLossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/warehouse/loss")
public class WarehouseLossController {
    @Autowired
    private WarehouseLossService warehouseLossService;

    @PostMapping
    public Map<String, Object> recordLoss(@RequestParam Integer productId,
                                          @RequestParam Integer quantity,
                                          @RequestParam String reason,
                                          @RequestParam Integer employeeId) {
        warehouseLossService.recordLoss(productId, quantity, reason, employeeId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        return result;
    }

    @GetMapping
    public List<ProductLossRecord> getAllLossRecords() {
        return warehouseLossService.getAllLossRecords();
    }
} 