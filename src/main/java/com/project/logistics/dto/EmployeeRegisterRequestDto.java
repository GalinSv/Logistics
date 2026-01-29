package com.project.logistics.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRegisterRequestDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private LocalDate dateOfBirth;
    private Byte typeOfEmpl; // 0 = ADMIN, 1 = EMPLOYEE
}
