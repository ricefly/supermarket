package com.mysupermarket.supermarket.backend.repository;

import com.mysupermarket.supermarket.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    java.util.List<Order> findByMemberId(Integer memberId);
} 