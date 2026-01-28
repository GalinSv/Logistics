package com.project.logistics.controller;

import com.project.logistics.dto.EmployeeByPhoneDto;
import com.project.logistics.dto.EmployeeCreateDto;
import com.project.logistics.dto.EmployeeDto;
import com.project.logistics.dto.EmployeeReportDto;
import com.project.logistics.service.EmployeeLookupService;
import com.project.logistics.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeLookupService employeeLookupService;

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

    @GetMapping("/by-phone")
    public EmployeeByPhoneDto getByPhone(@RequestParam String phone) {
        return employeeLookupService.getByPhone(phone);
    }
    @GetMapping("/report")
    public List<EmployeeReportDto> report() {
        return employeeService.getReport();
    }


}
