package com.project.logistics.controller;

import com.project.logistics.entity.Company;
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
    public List<Company> getAllCompanies() {
        return companyService.getAll();
    }

    @GetMapping("/{id}")
    public Company getCompanyById(@PathVariable Integer id) {
        return companyService.getById(id);
    }

    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        return companyService.create(company);
    }

    @DeleteMapping("/{id}")
    public void deleteCompany(@PathVariable Integer id) {
        companyService.delete(id);
    }
}
