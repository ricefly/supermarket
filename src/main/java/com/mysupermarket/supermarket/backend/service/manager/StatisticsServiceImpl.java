package com.mysupermarket.supermarket.backend.service.manager;

import com.mysupermarket.supermarket.backend.repository.OrderItemRepository;
import com.mysupermarket.supermarket.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Map<String, Object> getMonthlyStatistics(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);
        return getStatistics(start, end);
    }

    @Override
    public Map<String, Object> getYearlyStatistics(int year) {
        LocalDateTime start = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, 12, 31).atTime(23, 59, 59);
        return getStatistics(start, end);
    }

    private Map<String, Object> getStatistics(LocalDateTime start, LocalDateTime end) {
        // 统计各商品销售量、销售额
        List<Map<String, Object>> productStats = orderItemRepository.findProductSalesStatistics(start, end);
        // 销量最大商品
        Map<String, Object> maxSalesProduct = null;
        // 销售额最大商品
        Map<String, Object> maxRevenueProduct = null;
        int maxSales = 0;
        double maxRevenue = 0;
        for (Map<String, Object> stat : productStats) {
            int sales = ((Number) stat.get("totalQuantity")).intValue();
            double revenue = ((Number) stat.get("totalAmount")).doubleValue();
            if (sales > maxSales) {
                maxSales = sales;
                maxSalesProduct = stat;
            }
            if (revenue > maxRevenue) {
                maxRevenue = revenue;
                maxRevenueProduct = stat;
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("productStats", productStats);
        result.put("maxSalesProduct", maxSalesProduct);
        result.put("maxRevenueProduct", maxRevenueProduct);
        return result;
    }
} 