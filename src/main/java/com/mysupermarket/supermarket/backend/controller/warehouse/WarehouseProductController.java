package com.mysupermarket.supermarket.backend.controller.warehouse;

import com.mysupermarket.supermarket.backend.service.warehouse.WarehouseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/warehouse/products")
public class WarehouseProductController {
    @Autowired
    private WarehouseProductService warehouseProductService;

    @PostMapping("/{id}/stockin")
    public Map<String, Object> stockIn(@PathVariable Integer id, @RequestParam Integer quantity) {
        warehouseProductService.stockIn(id, quantity);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        return result;
    }
} 