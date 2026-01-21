package com.project.logistics.service.impl;

import com.project.logistics.dto.PricingDto;
import com.project.logistics.data.entity.Pricing;
import com.project.logistics.data.repository.PricingRepository;
import com.project.logistics.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final PricingRepository pricingRepository;

    @Override
    public List<PricingDto> getAllPricing() {
        return pricingRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private PricingDto mapToDto(Pricing pricing) {
        return PricingDto.builder()
                .id(pricing.getIdTransactions())
                .pricePerKilogram(pricing.getPricePerKilogram())
                .toAddress(pricing.getToAddress())
                .build();
    }
}
