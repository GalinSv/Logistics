package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeReportDto {
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String officeAddress;
    private Byte typeOfEmpl;
}