package com.project.logistics.controller;

import com.project.logistics.dto.PackageDto;
import com.project.logistics.service.PackageService;
import lombok.RequiredArgsConstructor;
import com.project.logistics.dto.PackageCreateDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @PostMapping
    public PackageDto create(@RequestBody PackageCreateDto dto) {
        return packageService.create(dto);
    }

    @GetMapping("/{id}")
    public PackageDto get(@PathVariable Integer id) {
        return packageService.getById(id);
    }

    @GetMapping
    public List<PackageDto> getAll() {
        return packageService.getAll();
    }

    @GetMapping("/undelivered")
    public List<PackageDto> undelivered() {
        return packageService.getUndelivered();
    }

    @PostMapping("/{id}/deliver")
    public PackageDto deliver(@PathVariable Integer id) {
        return packageService.markDelivered(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        packageService.delete(id);
    }
}
