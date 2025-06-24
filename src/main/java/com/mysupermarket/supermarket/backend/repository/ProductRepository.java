package com.mysupermarket.supermarket.backend.repository;

import com.mysupermarket.supermarket.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByProductCode(String productCode);
} 