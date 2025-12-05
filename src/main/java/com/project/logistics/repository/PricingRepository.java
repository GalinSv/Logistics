package com.project.logistics.repository;

import com.project.logistics.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {
}
