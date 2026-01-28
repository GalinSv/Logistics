package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientReportDto {
    private Integer clientId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
