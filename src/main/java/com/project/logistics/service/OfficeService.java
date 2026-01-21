package com.project.logistics.service;

import com.project.logistics.data.entity.Office;
import com.project.logistics.dto.OfficeCreateDto;
import com.project.logistics.dto.OfficeDto;

import java.util.List;

public interface OfficeService {

    OfficeDto create(OfficeCreateDto dto);

    OfficeDto getById(Integer idOffice);

    List<OfficeDto> getByCompany(Integer idCompany);

    List<OfficeDto> getAll();

    void delete(Integer idOffice);
}