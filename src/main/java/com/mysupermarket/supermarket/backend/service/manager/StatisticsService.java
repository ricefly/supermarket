package com.mysupermarket.supermarket.backend.service.manager;

import java.util.Map;

public interface StatisticsService {
    Map<String, Object> getMonthlyStatistics(int year, int month);
    Map<String, Object> getYearlyStatistics(int year);
} 