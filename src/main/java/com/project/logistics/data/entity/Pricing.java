package com.project.logistics.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
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


    @Positive(message = "pricePerKilogram must be positive")
    @Column(name = "pricePerKilogram", nullable = false)
    private Double pricePerKilogram;

    @Positive(message = "price must be positive")
    @Column(name = "price", nullable = false)
    private Double price;

    @OneToOne(mappedBy = "pricing")
    private Package aPackage;
}
