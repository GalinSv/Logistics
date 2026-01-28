package com.project.logistics.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageDto {

    private Integer idPackage;
    private Integer idSenderClient;
    private Integer idReceiverClient;
    private Integer idEmployee;
    private Double weight;
    private String addressToGo;
    private Boolean toOffice;
    private LocalDate dateOfDelivery;
    private Double price;
    private Integer officeId;
    private String officeAddress;
}
