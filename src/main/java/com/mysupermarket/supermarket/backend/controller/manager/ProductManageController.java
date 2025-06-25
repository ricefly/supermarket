package com.mysupermarket.supermarket.backend.controller.manager;

import com.mysupermarket.supermarket.backend.entity.Product;
import com.mysupermarket.supermarket.backend.service.manager.ProductManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/manager/products")
public class ProductManageController {
    @Autowired
    private ProductManageService productManageService;

    // 查询商品，支持关键词
    @GetMapping
    public List<Product> list(@RequestParam(required = false) String keyword) {
        return productManageService.listProducts(keyword);
    }

    // 添加商品
    @PostMapping
    public Product add(@RequestBody Product product) {
        return productManageService.addProduct(product);
    }

    // 修改商品
    @PutMapping("/{id}")
    public Product update(@PathVariable Integer id, @RequestBody Product product) {
        return productManageService.updateProduct(id, product);
    }

    // 商品下架
    @PostMapping("/{id}/off")
    public void off(@PathVariable Integer id) {
        productManageService.offProduct(id);
    }

    // 查询单个商品
    @GetMapping("/{id}")
    public Optional<Product> get(@PathVariable Integer id) {
        return productManageService.getProductById(id);
    }
} 