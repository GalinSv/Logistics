package com.project.logistics.service.impl;

import com.project.logistics.data.entity.Company;
import com.project.logistics.data.entity.Office;
import com.project.logistics.data.repository.CompanyRepository;
import com.project.logistics.data.repository.OfficeRepository;
import com.project.logistics.dto.OfficeCreateDto;
import com.project.logistics.dto.OfficeDto;
import com.project.logistics.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;

    @Override
    public OfficeDto create(OfficeCreateDto dto) {

        if (dto.getAddress() == null || dto.getAddress().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Address is required"
            );
        }

        Company company = companyRepository.findById(6)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Company not found"
                ));

        Office office = Office.builder()
                .address(dto.getAddress())
                .company(company)   // ✅ ВИНАГИ фирма 6
                .build();

        officeRepository.save(office);

        return OfficeDto.builder()
                .idOffice(office.getIdOffice())
                .address(office.getAddress())
                .idCompany(company.getIdCompany())
                .build();
    }


    @Override
    public OfficeDto getById(Integer idOffice) {
        Office office = officeRepository.findById(idOffice)
                .orElseThrow(() -> new RuntimeException("Office not found"));

        return mapToDto(office);
    }

    @Override
    public List<OfficeDto> getByCompany(Integer idCompany) {
        return officeRepository.findByCompany_IdCompany(idCompany)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<OfficeDto> getAll() {
        return officeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public void delete(Integer idOffice) {
        if (!officeRepository.existsById(idOffice)) {
            throw new RuntimeException("Office not found");
        }
        officeRepository.deleteById(idOffice);
    }

    private OfficeDto mapToDto(Office office) {
        return OfficeDto.builder()
                .idOffice(office.getIdOffice())
                .address(office.getAddress())
                .idCompany(office.getCompany().getIdCompany())
                .build();
    }
}
