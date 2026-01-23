package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {
    private String userType; // "CLIENT" or "EMPLOYEE"
    private String username;
    private String password;
}
