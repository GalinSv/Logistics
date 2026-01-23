package com.project.logistics.service;

import com.project.logistics.data.entity.Pricing;
import com.project.logistics.dto.PricingDto;
import com.project.logistics.dto.RevenueDto;

import java.time.LocalDate;
import java.util.List;

public interface PricingService {

    Pricing createPricing(Double weight, Boolean toOffice);

    RevenueDto getRevenue(LocalDate from, LocalDate to);
}
