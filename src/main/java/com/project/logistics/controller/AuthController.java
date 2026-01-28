package com.project.logistics.controller;

import com.project.logistics.dto.LoginRequestDto;
import com.project.logistics.dto.LoginResponseDto;
import com.project.logistics.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto req) {
        return authService.login(req);
    }
}
