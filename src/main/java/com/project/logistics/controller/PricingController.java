package com.project.logistics.controller;

import com.project.logistics.dto.PricingDto;
import com.project.logistics.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    @GetMapping
    public List<PricingDto> getAll() {
        return pricingService.getAllPricing();
    }
}
