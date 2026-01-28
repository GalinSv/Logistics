package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageCreateDto {

    private String senderPhone;
    private String senderFirstName;
    private String senderLastName;

    private String receiverPhone;
    private String receiverFirstName;
    private String receiverLastName;

    private Integer idEmployee;
    private Double weight;
    private Boolean toOffice;
    private String addressToGo;
    private Integer officeId;
}
