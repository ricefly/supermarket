package com.mysupermarket.supermarket.backend.service.warehouse;

import com.mysupermarket.supermarket.backend.entity.ProductLossRecord;
import com.mysupermarket.supermarket.backend.entity.Product;
import com.mysupermarket.supermarket.backend.repository.ProductLossRecordRepository;
import com.mysupermarket.supermarket.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WarehouseLossService {
    @Autowired
    private ProductLossRecordRepository lossRecordRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void recordLoss(Integer productId, Integer quantity, String reason, Integer employeeId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("商品不存在"));
        int newStock = product.getCurrentStock() - quantity;
        product.setCurrentStock(Math.max(newStock, 0));
        productRepository.save(product);

        ProductLossRecord record = new ProductLossRecord();
        record.setProductId(productId);
        record.setQuantityLost(quantity);
        record.setLossReason(reason);
        record.setLossDate(LocalDateTime.now());
        record.setRecordedByEmployeeId(employeeId);
        lossRecordRepository.save(record);
    }

    public List<ProductLossRecord> getAllLossRecords() {
        return lossRecordRepository.findAll();
    }
} 