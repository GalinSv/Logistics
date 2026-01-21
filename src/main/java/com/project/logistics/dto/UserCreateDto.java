package com.project.logistics.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private LocalDate dateOfBirth;
    private Integer idCompany;
}