package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingCreateDto {

    private Double pricePerKilogram;
    private Double price;
}
