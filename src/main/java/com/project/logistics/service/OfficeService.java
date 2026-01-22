package com.project.logistics.service;

import com.project.logistics.data.entity.Office;
import com.project.logistics.data.repository.OfficeRepository;
import com.project.logistics.dto.OfficeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;

    public List<OfficeDto> getAll() {
        return officeRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private OfficeDto toDto(Office o) {
        return OfficeDto.builder()
                .id(o.getIdOffice())
                .address(o.getAddress())
                .companyId(o.getCompany() != null ? o.getCompany().getIdCompany() : null)
                .companyName(o.getCompany() != null ? o.getCompany().getName() : null)
                .build();
    }
}
