package com.mysupermarket.supermarket.backend.service.manager;

import com.mysupermarket.supermarket.backend.entity.Product;
import com.mysupermarket.supermarket.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductManageService {
    @Autowired
    private ProductRepository productRepository;

    // 查询商品，支持关键词（名称或种类）
    public List<Product> listProducts(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.findByProductNameContainingOrCategoryContaining(keyword, keyword);
    }

    // 添加商品
    public Product addProduct(Product product) {
        product.setIsActive(true); // 默认在售
        return productRepository.save(product);
    }

    // 修改商品
    public Product updateProduct(Integer id, Product updated) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setProductCode(updated.getProductCode());
        product.setProductName(updated.getProductName());
        product.setCategory(updated.getCategory());
        product.setSpecification(updated.getSpecification());
        product.setPurchasePrice(updated.getPurchasePrice());
        product.setRetailPrice(updated.getRetailPrice());
        product.setCurrentStock(updated.getCurrentStock());
        product.setStockLocation(updated.getStockLocation());
        product.setLowStockThreshold(updated.getLowStockThreshold());
        product.setIsActive(updated.getIsActive());
        return productRepository.save(product);
    }

    // 商品下架
    public void offProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setIsActive(false);
        productRepository.save(product);
    }

    // 查询单个商品
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }
} 