package com.project.logistics.dto;
//TODO
public record LoginResponse(
        Integer userId,
        String username,
        String role,
        Integer clientId,
        Integer employeeId,
        String token
) {}