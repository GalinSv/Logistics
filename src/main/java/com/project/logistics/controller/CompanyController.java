package com.project.logistics.controller;

import com.project.logistics.data.entity.Company;
import com.project.logistics.dto.CompanyCreateDto;
import com.project.logistics.dto.CompanyDto;
import com.project.logistics.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public List<CompanyDto> getAll() {
        return companyService.getAll();
    }

    @PostMapping
    public CompanyDto create(@RequestBody CompanyCreateDto dto) {
        return companyService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        companyService.delete(id);
    }
}

