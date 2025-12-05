package com.project.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pricing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTransactions")
    private Integer idTransactions;

    @Column(name = "pricePerKilogram", nullable = false)
    private Double pricePerKilogram;

    @Column(name = "toAddress", nullable = false)
    private Integer toAddress; // 0/1 или друго числово кодиране

    @OneToOne(mappedBy = "pricing")
    private PackageEntity aPackage;
}
