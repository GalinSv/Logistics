package com.project.logistics.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class RegisterClientRequestDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
}
