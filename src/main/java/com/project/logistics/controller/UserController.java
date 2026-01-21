package com.project.logistics.controller;

import com.project.logistics.dto.UserCreateDto;
import com.project.logistics.dto.UserDto;
import com.project.logistics.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody UserCreateDto dto) {
        return userService.create(dto);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userService.delete(id);
    }
}