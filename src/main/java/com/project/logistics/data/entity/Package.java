package com.project.logistics.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "`package`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPackage")
    private Integer idPackage;

    @Positive
    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "addressToGo", columnDefinition = "LONGTEXT")
    private String addressToGo;

    @Column(name = "dateOfDelivery")
    private LocalDate dateOfDelivery;

    @Column(name = "toOffice", nullable = false)
    private Boolean toOffice;

    @ManyToOne
    @JoinColumn(name = "Client_idSender", nullable = false)
    private Client sender;

    @ManyToOne
    @JoinColumn(name = "Client_idReceiver", nullable = false)
    private Client receiver;

    @ManyToOne
    @JoinColumn(name = "Employee_idEmployee", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Transactions_idTransactions", nullable = false)
    private Pricing pricing;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PackageStatus status;

    @ManyToOne
    @JoinColumn(name = "Office_idOffice")
    private Office office;
}
