package com.project.logistics.controller;

import com.project.logistics.dto.EmployeeCreateDto;
import com.project.logistics.dto.EmployeeDto;
import com.project.logistics.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public EmployeeDto create(@RequestBody EmployeeCreateDto dto) {
        return employeeService.create(dto);
    }

    @GetMapping("/{id}")
    public EmployeeDto get(@PathVariable Integer id) {
        return employeeService.getById(id);
    }

    @GetMapping
    public List<EmployeeDto> getAll() {
        return employeeService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        employeeService.delete(id);
    }
}