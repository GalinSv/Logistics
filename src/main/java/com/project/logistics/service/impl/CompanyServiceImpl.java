package com.project.logistics.service.impl;

import com.project.logistics.data.entity.Company;
import com.project.logistics.data.repository.CompanyRepository;
import com.project.logistics.dto.CompanyCreateDto;
import com.project.logistics.dto.CompanyDto;
import com.project.logistics.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public CompanyDto create(CompanyCreateDto dto) {
        Company company = Company.builder()
                .name(dto.getName())
                .build();

        Company saved = companyRepository.save(company);
        return mapToDto(saved);
    }

    @Override
    public CompanyDto getById(Integer idCompany) {
        Company company = companyRepository.findById(idCompany)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return mapToDto(company);
    }

    @Override
    public List<CompanyDto> getAll() {
        return companyRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void delete(Integer idCompany) {
        if (!companyRepository.existsById(idCompany)) {
            throw new RuntimeException("Company not found");
        }
        companyRepository.deleteById(idCompany);
    }

    private CompanyDto mapToDto(Company company) {
        return CompanyDto.builder()
                .idCompany(company.getIdCompany())
                .name(company.getName())
                .build();
    }
}