package com.project.logistics.service.impl;

import com.project.logistics.dto.UserDto;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.UserRepository;
import com.project.logistics.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
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
}
