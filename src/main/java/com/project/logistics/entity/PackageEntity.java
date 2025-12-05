package com.project.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "package")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPackage")
    private Integer idPackage;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "addressToGo", columnDefinition = "LONGTEXT")
    private String addressToGo;

    @Column(name = "dateOfDelivery")
    private LocalDate dateOfDelivery;

    @Column(name = "toOffice", nullable = false)
    private Boolean toOffice; // TINYINT(1) -> Boolean

    @ManyToOne
    @JoinColumn(name = "Client_idSender", nullable = false)
    private Client sender;

    @ManyToOne
    @JoinColumn(name = "Client_idReceiver", nullable = false)
    private Client receiver;

    @ManyToOne
    @JoinColumn(name = "Employee_idEmployee", nullable = false)
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "Transactions_idTransactions")
    private Pricing pricing;
}
