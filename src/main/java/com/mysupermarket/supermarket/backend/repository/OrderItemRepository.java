package com.mysupermarket.supermarket.backend.repository;

import com.mysupermarket.supermarket.backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderId(Integer orderId);

    @Query("SELECT oi.productId AS productId, SUM(oi.quantity) AS totalQuantity, SUM(oi.itemTotal) AS totalAmount " +
           "FROM OrderItem oi, Order o " +
           "WHERE oi.orderId = o.orderId AND o.orderDate BETWEEN :start AND :end " +
           "GROUP BY oi.productId")
    List<Map<String, Object>> findProductSalesStatistics(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
} 