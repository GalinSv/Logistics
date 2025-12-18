package com.project.logistics.service;

import com.project.logistics.data.entity.Company;
import com.project.logistics.data.repository.CompanyRepository;
import com.project.logistics.dto.CompanyCreateDto;
import com.project.logistics.dto.CompanyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyDto> getAll() {
        return companyRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public CompanyDto create(CompanyCreateDto dto) {
        Company company = Company.builder()
                .name(dto.getName())
                .build();

        return toDto(companyRepository.save(company));
    }

    public void delete(Integer id) {
        companyRepository.deleteById(id);
    }

    private CompanyDto toDto(Company c) {
        return CompanyDto.builder()
                .id(c.getIdCompany())
                .name(c.getName())
                .build();
    }
}


