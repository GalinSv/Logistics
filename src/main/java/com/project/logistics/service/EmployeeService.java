package com.project.logistics.service;

import com.project.logistics.dto.EmployeeDto;
import com.project.logistics.entity.Employee;
import com.project.logistics.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

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
