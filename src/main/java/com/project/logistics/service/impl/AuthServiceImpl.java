package com.project.logistics.service.impl;

import com.project.logistics.data.entity.Employee;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.UserRepository;
import com.project.logistics.dto.LoginRequestDto;
import com.project.logistics.dto.LoginResponseDto;
import com.project.logistics.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto req) {

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid username or password"
                        )
                );

        // plain-text passwords (as stored in DB)
        if (user.getPassword() == null || !user.getPassword().equals(req.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid username or password"
            );
        }

        String requestedType =
                req.getUserType() == null ? "" : req.getUserType().toUpperCase();

        switch (requestedType) {

            case "CLIENT":
                if (user.getClient() == null) {
                    throw new ResponseStatusException(
                            HttpStatus.FORBIDDEN,
                            "This user is not a client"
                    );
                }
                return LoginResponseDto.builder()
                        .role("CLIENT")
                        .userId(user.getIdUser())
                        .clientId(user.getClient().getIdClient())
                        .employeeId(null)
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build();

            case "EMPLOYEE":
                Employee emp = user.getEmployee();
                if (emp == null) {
                    throw new ResponseStatusException(
                            HttpStatus.FORBIDDEN,
                            "This user is not an employee"
                    );
                }

                String role =
                        (emp.getTypeOfEmpl() != null && emp.getTypeOfEmpl() == 0)
                                ? "ADMIN"
                                : "EMPLOYEE";

                return LoginResponseDto.builder()
                        .role(role)
                        .userId(user.getIdUser())
                        .clientId(null)
                        .employeeId(emp.getIdEmployee())
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build();

            default:
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "userType must be CLIENT or EMPLOYEE"
                );
        }
    }
}
