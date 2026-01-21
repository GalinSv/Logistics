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
    public List<CompanyDto> getAll() {
        return companyRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public CompanyDto create(CompanyCreateDto dto) {
        Company company = Company.builder()
                .name(dto.getName())
                .build();

        return toDto(companyRepository.save(company));
    }

    @Override
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
