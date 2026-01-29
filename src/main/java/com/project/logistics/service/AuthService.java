package com.project.logistics.service;

import com.project.logistics.dto.LoginRequestDto;
import com.project.logistics.dto.LoginResponseDto;
import com.project.logistics.dto.RegisterRequestDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto request);
    void registerClient(RegisterRequestDto dto);
}
