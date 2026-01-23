package com.project.logistics.service.impl;

import com.project.logistics.data.entity.Company;
import com.project.logistics.data.repository.CompanyRepository;
import com.project.logistics.dto.UserCreateDto;
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
    private final CompanyRepository companyRepository;

    @Override
    public UserDto create(UserCreateDto dto) {
        Company company = companyRepository.findById(dto.getIdCompany())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        User user = User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword()) // hashing later
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .company(company)
                .build();

        User saved = userRepository.save(user);
        return mapToDto(saved);
    }

    @Override
    public UserDto getById(Integer idUser) {
        return userRepository.findById(idUser)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDto getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void delete(Integer idUser) {
        if (!userRepository.existsById(idUser)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(idUser);
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .idUser(user.getIdUser())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .idCompany(user.getCompany().getIdCompany())
                .build();
    }
}