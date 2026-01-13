//package com.project.logistics.service;
//
//import com.project.logistics.data.entity.User;
//import com.project.logistics.data.repository.ClientRepository;
//import com.project.logistics.data.repository.EmployeeRepository;
//import com.project.logistics.data.repository.UserRepository;
//import com.project.logistics.dto.LoginRequest;
//import com.project.logistics.dto.LoginResponse;
//import com.project.logistics.security.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
////TODO
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final ClientRepository clientRepository;
//    private final EmployeeRepository employeeRepository;
//    private final JwtUtil jwtUtil;
//
//    public LoginResponse login(LoginRequest request) {
//
//        User user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
//
//        if (!user.getPassword().equals(request.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }
//
//
//        var employee = employeeRepository.findByUser_IdUser(user.getIdUser());
//        if (employee.isPresent()) {
//            String token = jwtUtil.generateToken(user.getIdUser(), "EMPLOYEE");
//
//            return new LoginResponse(
//                    user.getIdUser(),
//                    user.getUsername(),
//                    "EMPLOYEE",
//                    null,
//                    employee.get().getIdEmployee(),
//                    token
//            );
//        }
//
//
//        var client = clientRepository.findByUser_IdUser(user.getIdUser());
//        if (client.isPresent()) {
//            String token = jwtUtil.generateToken(user.getIdUser(), "CLIENT");
//
//            return new LoginResponse(
//                    user.getIdUser(),
//                    user.getUsername(),
//                    "CLIENT",
//                    client.get().getIdClient(),
//                    null,
//                    token
//            );
//        }
//
//        throw new RuntimeException("User has no role");
//    }
//}