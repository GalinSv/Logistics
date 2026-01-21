package com.project.logistics.service;

import com.project.logistics.dto.CompanyCreateDto;
import com.project.logistics.dto.CompanyDto;
import java.util.List;

public interface CompanyService {

    CompanyDto create(CompanyCreateDto dto);

    CompanyDto getById(Integer idCompany);

    List<CompanyDto> getAll();

    void delete(Integer idCompany);
}
