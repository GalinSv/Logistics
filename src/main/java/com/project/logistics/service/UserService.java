package com.project.logistics.service;

import com.project.logistics.dto.UserDto;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getIdUser())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .companyId(user.getCompany() != null ? user.getCompany().getIdCompany() : null)
                .companyName(user.getCompany() != null ? user.getCompany().getName() : null)
                .build();
    }

    public User createUserForClient(String phone, String firstName, String lastName) {

        if (userRepository.findByPhone(phone).isPresent()) {
            throw new IllegalStateException("User with this phone already exists");
        }

        User user = User.builder()
                .username("c_" + phone)          // уникално, просто
                .password("pass123")             // учебно
                .firstName(firstName)
                .lastName(lastName)
                .email("auto_" + phone + "@local")
                .phone(phone)
                .build();

        return userRepository.save(user);
    }

}
