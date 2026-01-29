package com.project.logistics.controller;

import com.project.logistics.dto.LoginRequestDto;
import com.project.logistics.dto.LoginResponseDto;
import com.project.logistics.dto.RegisterRequestDto;
import com.project.logistics.dto.EmployeeRegisterRequestDto;
import com.project.logistics.service.AuthService;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.entity.Employee;
import com.project.logistics.data.entity.Company;
import com.project.logistics.data.repository.UserRepository;
import com.project.logistics.data.repository.EmployeeRepository;
import com.project.logistics.data.repository.CompanyRepository;
import com.project.logistics.data.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final OfficeRepository officeRepository;

    // ================= LOGIN =================
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto req) {
        return authService.login(req);
    }

    // ================= CLIENT REGISTER =================
    @PostMapping("/register")
    public void register(@RequestBody RegisterRequestDto dto) {
        authService.registerClient(dto);
    }

    // ================= EMPLOYEE REGISTER =================
    @PostMapping("/register-employee")
    public void registerEmployee(@RequestBody EmployeeRegisterRequestDto dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = userRepository.findByPhone(dto.getPhone()).orElse(null);

        if (user != null && !"temp".equals(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone already registered");
        }

        Company company = companyRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        if (user == null) {
            user = User.builder()
                    .username(dto.getUsername())
                    .firstName(dto.getFirstName())
                    .lastName(dto.getLastName())
                    .email(dto.getEmail())
                    .phone(dto.getPhone())
                    .password(dto.getPassword())
                    .dateOfBirth(dto.getDateOfBirth())
                    .company(company)
                    .build();
        } else {
            user.setUsername(dto.getUsername());
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setPassword(dto.getPassword());
            user.setDateOfBirth(dto.getDateOfBirth());
        }

        userRepository.save(user);

        Employee employee = Employee.builder()
                .user(user)
                .company(company)
                .office(officeRepository.findById(1).orElseThrow())
                .typeOfEmpl(dto.getTypeOfEmpl())
                .build();

        employeeRepository.save(employee);
    }


    // ================= LIVE CHECKS =================
    @GetMapping("/check-username")
    public boolean checkUsername(@RequestParam String username) {
        return !userRepository.existsByUsername(username);
    }

    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return !userRepository.existsByEmail(email);
    }

    @GetMapping("/check-phone")
    public Map<String, Boolean> checkPhone(@RequestParam String phone) {

        Optional<User> userOpt = userRepository.findByPhone(phone);

        boolean allowed = true;

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!"temp".equals(user.getPassword())) {
                allowed = false;
            }
        }

        return Map.of("allowed", allowed);
    }
}
