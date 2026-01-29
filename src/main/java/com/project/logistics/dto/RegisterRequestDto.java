package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDto {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
}
