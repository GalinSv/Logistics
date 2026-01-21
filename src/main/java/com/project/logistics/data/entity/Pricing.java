package com.project.logistics.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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


    @Column(name = "pricePerKilogram")
    private Double pricePerKilogram;

    @Column(name = "price")
    private Double price;

    @OneToOne(mappedBy = "pricing")
    private Package aPackage;
}
