package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingDto {

    private Integer id;
    private Double pricePerKilogram;
    private Integer toAddress;
}
