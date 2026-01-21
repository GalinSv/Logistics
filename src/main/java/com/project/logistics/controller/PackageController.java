package com.project.logistics.controller;

import com.project.logistics.dto.PackageDto;
import com.project.logistics.service.PackageService;
import lombok.RequiredArgsConstructor;
import com.project.logistics.dto.PackageCreateDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @GetMapping
    public List<PackageDto> getAll() {
        return packageService.getAllPackages();
    }
    @GetMapping("/sender/{senderId}")
    public List<PackageDto> getBySender(@PathVariable Integer senderId) {
        return packageService.getPackagesBySender(senderId);
    }

    @PostMapping
    public PackageDto create(@RequestBody PackageCreateDto dto) {
        return packageService.createPackage(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        packageService.deletePackage(id);
    }
    @GetMapping("/{id}/price")
    public double getPackagePrice(@PathVariable Integer id) {
        return packageService.calculatePackagePrice(id);
    }
    @GetMapping("/employee/{employeeId}")
    public List<PackageDto> getByEmployee(@PathVariable Integer employeeId) {
        return packageService.getPackagesByEmployee(employeeId);
    }
    @GetMapping("/undelivered")
    public List<PackageDto> getUndelivered() {
        return packageService.getUndeliveredPackages();
    }
    @GetMapping("/receiver/{clientId}")
    public List<PackageDto> getReceivedByClient(@PathVariable Integer clientId) {
        return packageService.getReceivedByClient(clientId);
    }
    @GetMapping("/revenue")
    public double getRevenue(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return packageService.getRevenue(from, to);
    }

    @GetMapping("/client/{clientId}/sent")
    public List<PackageDto> getSentByClient(@PathVariable Integer clientId) {
        return packageService.getPackagesBySender(clientId);
    }


    @GetMapping("/employee/revenue/{employeeId}")
    public double getRevenueByEmployee(@PathVariable Integer employeeId) {
        return packageService.getRevenueByEmployee(employeeId);
    }
    @GetMapping("/my")
    public List<PackageDto> getMyPackages(
            @RequestParam boolean isEmployee,
            @RequestParam(required = false) Integer clientId
    ) {
        return packageService.getPackagesForUser(isEmployee, clientId);
    }

}
