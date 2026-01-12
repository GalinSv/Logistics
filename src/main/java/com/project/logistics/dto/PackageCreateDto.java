package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageCreateDto {

    private Double weight;
    private Boolean toOffice;
    private String deliveryAddress;

    private Integer senderClientId;
    private Integer receiverClientId;
    private Integer employeeId;
    private Integer pricingId;
}
