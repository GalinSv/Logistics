package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeDto {
    private Integer id;
    private String address;
    private Integer companyId;
    private String companyName;
}
