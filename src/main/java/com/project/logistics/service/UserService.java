package com.project.logistics.service;

import com.project.logistics.dto.UserCreateDto;
import com.project.logistics.dto.UserDto;
import java.util.List;

public interface UserService {

    UserDto create(UserCreateDto dto);

    UserDto getById(Integer idUser);

    UserDto getByUsername(String username);

    List<UserDto> getAll();

    void delete(Integer idUser);
}