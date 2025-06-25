package com.mysupermarket.supermarket.backend.service.warehouse;

import com.mysupermarket.supermarket.backend.entity.Product;
import com.mysupermarket.supermarket.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarehouseProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void stockIn(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("商品不存在"));
        int newStock = product.getCurrentStock() + quantity;
        product.setCurrentStock(newStock);
        productRepository.save(product);
    }
} 