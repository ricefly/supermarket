package com.mysupermarket.supermarket.backend.service.manager;

import com.mysupermarket.supermarket.backend.entity.Employee;
import com.mysupermarket.supermarket.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeManageService {
    @Autowired
    private EmployeeRepository employeeRepository;

    // 查询所有非经理员工
    public List<Employee> getAllEmployeesExceptManager(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return employeeRepository.findByRoleNot("manager");
        }
        // 支持用户名或姓名模糊查询
        return employeeRepository.findByRoleNotAndUsernameContainingOrRoleNotAndNameContaining("manager", keyword, "manager", keyword);
    }

    // 新增员工
    public Employee addEmployee(Employee employee) {
        if ("manager".equals(employee.getRole())) {
            throw new IllegalArgumentException("不能添加经理账号");
        }
        return employeeRepository.save(employee);
    }

    // 编辑员工
    public Employee updateEmployee(Integer id, Employee updated) {
        Employee emp = employeeRepository.findById(id).orElseThrow();
        if ("manager".equals(emp.getRole())) {
            throw new IllegalArgumentException("不能编辑经理账号");
        }
        emp.setName(updated.getName());
        emp.setRole(updated.getRole());
        emp.setStatus(updated.getStatus());
        // 密码如需修改可加判断
        return employeeRepository.save(emp);
    }

    // 禁用员工
    public void disableEmployee(Integer id) {
        Employee emp = employeeRepository.findById(id).orElseThrow();
        if ("manager".equals(emp.getRole())) {
            throw new IllegalArgumentException("不能禁用经理账号");
        }
        emp.setStatus("禁用");
        employeeRepository.save(emp);
    }
} 