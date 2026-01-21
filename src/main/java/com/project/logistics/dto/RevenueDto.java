package com.project.logistics.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueDto {

    private LocalDate from;
    private LocalDate to;
    private Double totalRevenue;
}
