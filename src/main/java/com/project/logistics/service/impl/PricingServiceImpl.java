package com.project.logistics.service.impl;

import com.project.logistics.data.repository.PackageRepository;
import com.project.logistics.dto.PricingDto;
import com.project.logistics.data.entity.Pricing;
import com.project.logistics.data.repository.PricingRepository;
import com.project.logistics.dto.RevenueDto;
import com.project.logistics.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final PricingRepository pricingRepository;
    private final PackageRepository packageRepository;

    private static final double PRICE_PER_KG = 5.0;
    private static final double ADDRESS_MULTIPLIER = 1.2;

    @Override
    public Pricing createPricing(Double weight, Boolean toOffice) {
        double basePrice = weight * PRICE_PER_KG;

        if (!toOffice) {
            basePrice *= ADDRESS_MULTIPLIER;
        }

        Pricing pricing = Pricing.builder()
                .pricePerKilogram(PRICE_PER_KG)
                .price(basePrice)
                .build();

        return pricingRepository.save(pricing);
    }

    @Override
    public RevenueDto getRevenue(LocalDate from, LocalDate to) {
        Double revenue = pricingRepository.sumRevenueBetweenDates(from, to);

        return RevenueDto.builder()
                .from(from)
                .to(to)
                .totalRevenue(revenue != null ? revenue : 0.0)
                .build();
    }
}