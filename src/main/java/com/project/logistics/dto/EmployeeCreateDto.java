package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeCreateDto {

    private Integer idUser;
    private Integer idCompany;
    private Integer idOffice;
    private Byte typeOfEmpl;
}