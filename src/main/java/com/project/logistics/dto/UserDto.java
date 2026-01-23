package com.project.logistics.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Integer idUser;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Integer idCompany;
}