package com.project.logistics.service.impl;

import com.project.logistics.data.entity.Company;
import com.project.logistics.data.entity.Office;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.*;
import com.project.logistics.dto.EmployeeCreateDto;
import com.project.logistics.dto.EmployeeDto;
import com.project.logistics.data.entity.Employee;
import com.project.logistics.dto.EmployeeReportDto;
import com.project.logistics.dto.PackageDto;
import com.project.logistics.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final OfficeRepository officeRepository;
    private final PackageRepository packageRepository;

    @Override
    public EmployeeDto create(EmployeeCreateDto dto) {
        User user = userRepository.findById(dto.getIdUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findById(dto.getIdCompany())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Office office = officeRepository.findById(dto.getIdOffice())
                .orElseThrow(() -> new RuntimeException("Office not found"));

        Employee employee = Employee.builder()
                .user(user)
                .company(company)
                .office(office)
                .typeOfEmpl(dto.getTypeOfEmpl())
                .build();

        Employee saved = employeeRepository.save(employee);
        return mapToDto(saved);
    }

    @Override
    public EmployeeDto getById(Integer idEmployee) {
        return employeeRepository.findById(idEmployee)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public List<EmployeeDto> getAll() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }


    @Override
    public void delete(Integer idEmployee) {
        if (!employeeRepository.existsById(idEmployee)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(idEmployee);
    }

    private EmployeeDto mapToDto(Employee employee) {
        return EmployeeDto.builder()
                .idEmployee(employee.getIdEmployee())
                .idUser(employee.getUser().getIdUser())
                .idCompany(employee.getCompany().getIdCompany())
                .idOffice(employee.getOffice().getIdOffice())
                .typeOfEmpl(employee.getTypeOfEmpl())
                .build();
    }

    @Override
    public List<EmployeeReportDto> getReport() {
        return employeeRepository.findAllForReport();
    }


}
