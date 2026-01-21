package com.project.logistics.service;

import com.project.logistics.dto.CompanyCreateDto;
import com.project.logistics.dto.CompanyDto;
import java.util.List;

public interface CompanyService {

    List<CompanyDto> getAll();

    CompanyDto create(CompanyCreateDto dto);

    void delete(Integer id);
}
