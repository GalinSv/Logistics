package com.project.logistics.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageDto {

    private Integer id;
    private Double weight;
    private String addressToGo;
    private LocalDate dateOfDelivery;
    private Boolean toOffice;

    private Integer senderId;
    private String senderUsername;

    private Integer receiverId;
    private String receiverUsername;

    private Integer employeeId;
    private String employeeUsername;
    private String employeeFullName;

    private Integer pricingId;
    private Double pricePerKilogram;
    private Integer pricingToAddress;
    private String status;

    private String senderFullName;
    private String receiverFullName;

    private Integer officeId;
    private String officeAddress;
}
