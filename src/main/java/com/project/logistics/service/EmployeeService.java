package com.project.logistics.service;

import com.project.logistics.dto.EmployeeCreateDto;
import com.project.logistics.dto.EmployeeDto;
import com.project.logistics.dto.EmployeeReportDto;
import com.project.logistics.dto.PackageDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto create(EmployeeCreateDto dto);

    EmployeeDto getById(Integer idEmployee);

    List<EmployeeDto> getAll();

    void delete(Integer idEmployee);

    List<EmployeeReportDto> getReport();
}