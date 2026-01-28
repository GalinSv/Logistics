package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientByPhoneDto {
    private Boolean exists;
    private Integer clientId;
    private String phone;
    private String firstName;
    private String lastName;
}
