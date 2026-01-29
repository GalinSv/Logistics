package com.project.logistics.service.impl;

import com.project.logistics.data.entity.Client;
import com.project.logistics.data.entity.Company;
import com.project.logistics.data.entity.Employee;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.ClientRepository;
import com.project.logistics.data.repository.CompanyRepository;
import com.project.logistics.data.repository.UserRepository;
import com.project.logistics.dto.LoginRequestDto;
import com.project.logistics.dto.LoginResponseDto;
import com.project.logistics.dto.RegisterRequestDto;
import com.project.logistics.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;

    // ================= LOGIN =================
    @Override
    public LoginResponseDto login(LoginRequestDto req) {

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid username or password"
                ));

        // plain-text password check (NO SECURITY)
        if (user.getPassword() == null ||
                !user.getPassword().equals(req.getPassword())) {
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

    // ================= REGISTER CLIENT =================
    @Override
    public void registerClient(RegisterRequestDto dto) {

        // 0) Basic sanity (optional, but helpful)
        if (dto.getUsername() == null || dto.getUsername().isBlank() ||
                dto.getEmail() == null || dto.getEmail().isBlank() ||
                dto.getPhone() == null || dto.getPhone().isBlank() ||
                dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required fields");
        }

        // 1) username exists -> error
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        // 2) email exists -> error
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // 3) phone exists?
        User existing = userRepository.findByPhone(dto.getPhone()).orElse(null);

        if (existing != null) {

            // phone exists & password == "temp" -> upgrade the user
            if ("temp".equals(existing.getPassword())) {
                existing.setUsername(dto.getUsername());
                existing.setFirstName(dto.getFirstName());
                existing.setLastName(dto.getLastName());
                existing.setEmail(dto.getEmail());
                existing.setPassword(dto.getPassword()); // plain-text (no security)

                userRepository.save(existing);

                // ensure a Client exists (safety)
                if (existing.getClient() == null) {
                    clientRepository.save(
                            Client.builder()
                                    .user(existing)
                                    .build()
                    );
                }

                return;
            }

            // phone exists & password != "temp" -> already registered
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User with this phone is already registered"
            );
        }

        // 4) phone does NOT exist -> create new User + Client
        Company company = companyRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Company not found"
                ));

        User user = User.builder()
                .username(dto.getUsername())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(dto.getPassword()) // plain-text
                .company(company)
                .build();

        userRepository.save(user);

        clientRepository.save(
                Client.builder()
                        .user(user)
                        .build()
        );
    }
}
