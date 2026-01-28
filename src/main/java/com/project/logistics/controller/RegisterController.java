package com.project.logistics.controller;
import com.project.logistics.data.entity.Client;
import com.project.logistics.data.entity.Employee;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.ClientRepository;
import com.project.logistics.data.repository.EmployeeRepository;
import com.project.logistics.data.repository.UserRepository;
import com.project.logistics.dto.RegisterClientRequestDto;
import com.project.logistics.dto.RegisterEmployeeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})

public class RegisterController {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;

    @PostMapping("/register/client")
    public String registerClient(@RequestBody RegisterClientRequestDto req) {
        if (req.getUsername() == null || req.getUsername().isBlank()
                || req.getPassword() == null || req.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password are required.");
        }

        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }

        User user = User.builder()
                .username(req.getUsername())
                .password(req.getPassword())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .address(req.getAddress())
                .build();

        user = userRepository.save(user);

        Client client = Client.builder()
                .user(user)
                .build();

        clientRepository.save(client);

        return "OK";

    }

    @PostMapping("/register/employee")
    public String registerEmployee(@RequestBody RegisterEmployeeRequestDto req) {
        if (req.getAdminUserId() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "adminUserId is required.");
        }
        User admin = userRepository.findById(req.getAdminUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin not found."));

        if (admin.getEmployee() == null || admin.getEmployee().getTypeOfEmpl() == null || admin.getEmployee().getTypeOfEmpl() != 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN can register employees.");
        }

        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }

        User user = User.builder()
                .username(req.getUsername())
                .password(req.getPassword()) // plain-text както е при login
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .build();

        user = userRepository.save(user);

        Employee employee = Employee.builder()
                .user(user)
                .typeOfEmpl(
                        (byte) (req.getTypeOfEmpl() == null ? 1 : req.getTypeOfEmpl())
                )
                .build();


        employeeRepository.save(employee);

        return "OK";

    }
}
