package com.project.logistics.data.repository;

import com.project.logistics.data.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {

    @Query("""
    SELECT SUM(p.price)
    FROM Pricing p
    JOIN Package pkg ON pkg.pricing = p
    WHERE pkg.dateOfDelivery BETWEEN :from AND :to
""")
Double sumRevenueBetweenDates(LocalDate from, LocalDate to);

    
    
    
}
