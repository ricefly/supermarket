package com.mysupermarket.supermarket.backend.controller.manager;

import com.mysupermarket.supermarket.backend.entity.Employee;
import com.mysupermarket.supermarket.backend.service.manager.EmployeeManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/employees")
public class EmployeeManageController {
    @Autowired
    private EmployeeManageService employeeManageService;

    // 查询所有非经理员工
    @GetMapping
    public List<Employee> list(@RequestParam(required = false) String keyword) {
        return employeeManageService.getAllEmployeesExceptManager(keyword);
    }

    // 新增员工
    @PostMapping
    public Employee add(@RequestBody Employee employee) {
        return employeeManageService.addEmployee(employee);
    }

    // 编辑员工
    @PutMapping("/{id}")
    public Employee update(@PathVariable Integer id, @RequestBody Employee employee) {
        return employeeManageService.updateEmployee(id, employee);
    }

    // 禁用员工
    @PostMapping("/{id}/disable")
    public void disable(@PathVariable Integer id) {
        employeeManageService.disableEmployee(id);
    }
} 