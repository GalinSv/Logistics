package com.project.logistics.service.impl;

import com.project.logistics.dto.EmployeeDto;
import com.project.logistics.data.entity.Employee;
import com.project.logistics.data.repository.EmployeeRepository;
import com.project.logistics.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private EmployeeDto mapToDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getIdEmployee())
                .typeOfEmpl(employee.getTypeOfEmpl())
                .userId(employee.getUser() != null ? employee.getUser().getIdUser() : null)
                .username(employee.getUser() != null ? employee.getUser().getUsername() : null)
                .companyId(employee.getCompany() != null ? employee.getCompany().getIdCompany() : null)
                .companyName(employee.getCompany() != null ? employee.getCompany().getName() : null)
                .officeId(employee.getOffice() != null ? employee.getOffice().getIdOffice() : null)
                .officeAddress(employee.getOffice() != null ? employee.getOffice().getAddress() : null)
                .build();
    }
}
