package com.project.logistics.service;

import com.project.logistics.dto.EmployeeByPhoneDto;

public interface EmployeeLookupService {
    EmployeeByPhoneDto getByPhone(String phone);
}
