package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeByPhoneDto {
    private Boolean exists;
    private Integer employeeId;
    private String phone;
    private String firstName;
    private String lastName;
}
