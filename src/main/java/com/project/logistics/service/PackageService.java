package com.project.logistics.service;

import com.project.logistics.dto.PackageCreateDto;
import com.project.logistics.dto.PackageDto;

import java.time.LocalDate;
import java.util.List;

public interface PackageService {

    List<PackageDto> getAllPackages();

    List<PackageDto> getPackagesBySender(Integer senderId);

    PackageDto createPackage(PackageCreateDto dto);

    void deletePackage(Integer id);

    double calculatePackagePrice(Integer packageId);

    List<PackageDto> getPackagesByEmployee(Integer employeeId);

    List<PackageDto> getUndeliveredPackages();

    List<PackageDto> getReceivedByClient(Integer clientId);

    double getRevenue(LocalDate from, LocalDate to);

    double getRevenueByEmployee(Integer employeeId);

    List<PackageDto> getPackagesForUser(boolean isEmployee, Integer clientId);
}
