package com.mysupermarket.supermarket.backend.repository;

import com.mysupermarket.supermarket.backend.entity.ProductLossRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface ProductLossRecordRepository extends JpaRepository<ProductLossRecord, Integer> {
} 