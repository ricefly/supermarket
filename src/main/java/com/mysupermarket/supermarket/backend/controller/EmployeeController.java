package com.mysupermarket.supermarket.backend.controller;

import com.mysupermarket.supermarket.backend.dto.EmployeeLoginRequest;
import com.mysupermarket.supermarket.backend.dto.EmployeeLoginResponse;
import com.mysupermarket.supermarket.backend.entity.Employee;
import com.mysupermarket.supermarket.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public EmployeeLoginResponse login(@RequestBody EmployeeLoginRequest request) {
        return employeeService.login(request);
    }

    @GetMapping("/info")
    public Object getEmployeeInfo(@RequestHeader("token") String token) {
        Integer employeeId = employeeService.getEmployeeIdByToken(token);
        if (employeeId == null) {
            return Map.of("code", 1, "msg", "未登录或token无效");
        }

        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            return Map.of("code", 1, "msg", "员工不存在");
        }

        Map<String, Object> info = new HashMap<>();
        info.put("code", 0);
        info.put("data", Map.of(
            "username", employee.getUsername(),
            "name", employee.getName(),
            "role", employee.getRole(),
            "status", employee.getStatus()
        ));
        return info;
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(@RequestHeader("token") String token) {
        employeeService.logout(token);
        return Map.of("code", 0, "msg", "退出成功");
    }
} 