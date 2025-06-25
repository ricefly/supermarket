package com.mysupermarket.supermarket.backend.controller.manager;

import com.mysupermarket.supermarket.backend.service.manager.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/manager/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    // 月度商品销售统计
    @GetMapping("/monthly")
    public Map<String, Object> getMonthlyStatistics(@RequestParam int year, @RequestParam int month) {
        return statisticsService.getMonthlyStatistics(year, month);
    }

    // 年度商品销售统计
    @GetMapping("/yearly")
    public Map<String, Object> getYearlyStatistics(@RequestParam int year) {
        return statisticsService.getYearlyStatistics(year);
    }
} 