package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageCreateDto {

    private Integer idSenderClient;
    private Integer idReceiverClient;
    private Integer idEmployee;
    private Double weight;
    private String addressToGo;
    private Boolean toOffice;
}