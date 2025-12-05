package com.project.logistics.service;

import com.project.logistics.entity.Company;
import com.project.logistics.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public Company getById(Integer id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public Company create(Company company) {
        return companyRepository.save(company);
    }

    public void delete(Integer id) {
        companyRepository.deleteById(id);
    }
}

