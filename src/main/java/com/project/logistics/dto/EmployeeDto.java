package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {

    private Integer idEmployee;
    private Integer idUser;
    private Integer idCompany;
    private Integer idOffice;
    private Byte typeOfEmpl;
}