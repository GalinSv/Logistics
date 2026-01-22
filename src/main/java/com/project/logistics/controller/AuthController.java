//package com.project.logistics.controller;
//
//import com.project.logistics.dto.LoginRequest;
//import com.project.logistics.dto.LoginResponse;
//import com.project.logistics.service.AuthService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
////TODO
//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/login")
//    public LoginResponse login(@RequestBody LoginRequest request) {
//        return authService.login(request);
//    }
//}
package com.project.logistics.controller;

import com.project.logistics.data.entity.Employee;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.UserRepository;
import com.project.logistics.dto.LoginRequestDto;
import com.project.logistics.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5175"})
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto req) {

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        // plain-text passwords (както е при вас в БД)
        if (user.getPassword() == null || !user.getPassword().equals(req.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        String requestedType = req.getUserType() == null ? "" : req.getUserType().toUpperCase();

        if ("CLIENT".equals(requestedType)) {
            if (user.getClient() == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This user is not a client");
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
        }

        if ("EMPLOYEE".equals(requestedType)) {
            Employee emp = user.getEmployee();
            if (emp == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This user is not an employee");
            }

            String role = (emp.getTypeOfEmpl() != null && emp.getTypeOfEmpl() == 0) ? "ADMIN" : "EMPLOYEE";

            return LoginResponseDto.builder()
                    .role(role)
                    .userId(user.getIdUser())
                    .clientId(null)
                    .employeeId(emp.getIdEmployee())
                    .username(user.getUsername())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userType must be CLIENT or EMPLOYEE");
    }
}
