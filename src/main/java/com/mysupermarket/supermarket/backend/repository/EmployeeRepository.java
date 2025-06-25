package com.mysupermarket.supermarket.backend.repository;

import com.mysupermarket.supermarket.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
 
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByUsername(String username);
    List<Employee> findByRoleNot(String role);
    List<Employee> findByRoleNotAndUsernameContainingOrRoleNotAndNameContaining(String role1, String username, String role2, String name);
} 