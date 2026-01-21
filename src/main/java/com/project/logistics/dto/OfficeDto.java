package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeDto {

    private Integer idOffice;
    private String address;
    private Integer idCompany;
}
