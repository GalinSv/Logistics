package com.project.logistics.controller;

import com.project.logistics.dto.PackageCreateDto;
import com.project.logistics.dto.PackageDto;
import com.project.logistics.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @GetMapping
    public List<PackageDto> getAll() {
        return packageService.getAllPackages();
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

    @GetMapping("/undelivered")
    public List<PackageDto> getUndelivered() {
        return packageService.getUndeliveredPackages();
    }

    @GetMapping("/sender/{senderId}")
    public List<PackageDto> getBySender(@PathVariable Integer senderId) {
        return packageService.getPackagesBySender(senderId);
    }

    @GetMapping("/receiver/{clientId}")
    public List<PackageDto> getReceivedByClient(@PathVariable Integer clientId) {
        return packageService.getReceivedByClient(clientId);
    }

    @GetMapping("/revenue")
    public double getRevenue(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        return packageService.getRevenue(from, to);
    }

    @PatchMapping("/{id}/status")
    public PackageDto updateStatus(@PathVariable Integer id, @RequestParam String status) {
        return packageService.updateStatus(id, status);
    }

    @GetMapping("/client/{clientId}/all")
    public List<PackageDto> getAllForClient(@PathVariable Integer clientId) {
        return packageService.getAllPackagesForClient(clientId);
    }
}
