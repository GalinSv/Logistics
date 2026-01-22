package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCreateDto {
    private String phone;
    private String firstName;
    private String lastName;
}