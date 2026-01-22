package com.project.logistics.data.repository;

import com.project.logistics.data.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByUser_Username(String username);
}
