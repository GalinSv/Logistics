package com.project.logistics.service.impl;
import com.project.logistics.data.entity.Employee;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.EmployeeRepository;
import com.project.logistics.data.repository.UserRepository;
import com.project.logistics.dto.EmployeeByPhoneDto;
import com.project.logistics.service.EmployeeLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeLookupServiceImpl implements EmployeeLookupService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeByPhoneDto getByPhone(String phone) {

        User user = userRepository.findByPhone(phone).orElse(null);

        if (user == null) {
            return EmployeeByPhoneDto.builder()
                    .exists(false)
                    .phone(phone)
                    .build();
        }

        Employee employee =
                employeeRepository.findByUser_IdUser(user.getIdUser())
                        .orElse(null);

        return EmployeeByPhoneDto.builder()
                .exists(employee != null)
                .employeeId(employee != null ? employee.getIdEmployee() : null)
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
