package com.project.logistics.controller;

import com.project.logistics.dto.PackageDto;
import com.project.logistics.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
