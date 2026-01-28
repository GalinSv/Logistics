package com.project.logistics.service;

import com.project.logistics.dto.PackageCreateDto;
import com.project.logistics.dto.PackageDto;
import com.project.logistics.dto.PackageReportDto;

import java.time.LocalDate;
import java.util.List;

public interface PackageService {

    PackageDto create(PackageCreateDto dto);

    PackageDto getById(Integer idPackage);

    List<PackageDto> getAll();

    List<PackageDto> getByEmployee(Integer idEmployee);

    List<PackageDto> getSentByClient(Integer idClient);

    List<PackageDto> getReceivedByClient(Integer idClient);

    List<PackageDto> getUndelivered();

    PackageDto markDelivered(Integer idPackage);

    void delete(Integer idPackage);

    List<PackageReportDto> getReport();

}
