package com.project.logistics.dto;

import com.project.logistics.data.entity.PackageStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageReportDto {

    private Integer packageId;

    private String senderPhone;
    private String receiverPhone;
    private String employeePhone;

    private Double weight;
    private Boolean toOffice;

    private String addressToGo;
    private String officeAddress;

    private LocalDate dateOfDelivery;
    private PackageStatus status;
}
