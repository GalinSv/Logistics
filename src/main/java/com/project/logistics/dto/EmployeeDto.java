package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {

    private Integer id;
    private Byte typeOfEmpl;

    private Integer userId;
    private String username;

    private Integer companyId;
    private String companyName;

    private Integer officeId;
    private String officeAddress;
}
