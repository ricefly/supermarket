package com.mysupermarket.supermarket.backend.service;

import com.mysupermarket.supermarket.backend.dto.EmployeeLoginRequest;
import com.mysupermarket.supermarket.backend.dto.EmployeeLoginResponse;
import com.mysupermarket.supermarket.backend.entity.Employee;
import com.mysupermarket.supermarket.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmployeeService {
    public static final String STATUS_ACTIVE = "在职";
    public static final String STATUS_INACTIVE = "离职";
    
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_CASHIER = "cashier";
    public static final String ROLE_WAREHOUSE = "warehouse";

    @Autowired
    private EmployeeRepository employeeRepository;

    // 用于保存token和员工ID的映射
    private static final ConcurrentHashMap<String, Integer> tokenEmployeeMap = new ConcurrentHashMap<>();

    public EmployeeLoginResponse login(EmployeeLoginRequest request) {
        Employee employee = employeeRepository.findByUsername(request.getUsername());
        
        if (employee == null) {
            return new EmployeeLoginResponse(1, "用户名不存在", null, null);
        }
        
        if (!employee.getPasswordHash().equals(request.getPassword())) {
            return new EmployeeLoginResponse(1, "密码错误", null, null);
        }
        
        if (!STATUS_ACTIVE.equals(employee.getStatus())) {
            return new EmployeeLoginResponse(1, "账号已停用", null, null);
        }

        // 登录成功，生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenEmployeeMap.put(token, employee.getEmployeeId());
        
        return new EmployeeLoginResponse(0, "登录成功", token, employee.getRole());
    }

    // 根据token获取员工ID
    public Integer getEmployeeIdByToken(String token) {
        return tokenEmployeeMap.get(token);
    }

    // 根据员工ID获取员工信息
    public Employee getEmployeeById(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    // 注销token
    public void logout(String token) {
        tokenEmployeeMap.remove(token);
    }
} 