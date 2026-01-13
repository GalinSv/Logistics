package com.project.logistics.service;

import com.project.logistics.dto.PricingDto;
import com.project.logistics.data.entity.Pricing;
import com.project.logistics.data.repository.PricingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingService {

    private final PricingRepository pricingRepository;

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
