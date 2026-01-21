package com.project.logistics.controller;

import com.project.logistics.data.entity.Company;
import com.project.logistics.dto.CompanyCreateDto;
import com.project.logistics.dto.CompanyDto;
import com.project.logistics.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public CompanyDto create(@RequestBody CompanyCreateDto dto) {
        return companyService.create(dto);
    }

    @GetMapping("/{id}")
    public CompanyDto get(@PathVariable Integer id) {
        return companyService.getById(id);
    }

    @GetMapping
    public List<CompanyDto> getAll() {
        return companyService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        companyService.delete(id);
    }
}

