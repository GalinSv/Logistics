package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String role; // "CLIENT", "EMPLOYEE", "ADMIN"
    private Integer userId;
    private Integer clientId;    // null if not client
    private Integer employeeId;  // null if not employee
    private String username;
    private String firstName;
    private String lastName;

}
