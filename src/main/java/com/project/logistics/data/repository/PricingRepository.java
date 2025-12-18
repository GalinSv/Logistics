package com.project.logistics.data.repository;

import com.project.logistics.data.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {
}
