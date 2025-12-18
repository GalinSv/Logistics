package com.project.logistics.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEmployee")
    private Integer idEmployee;

    @Column(name = "typeOfEmpl", nullable = false)
    private Byte typeOfEmpl;

    @OneToOne
    @JoinColumn(name = "User_idUser", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "User_Company_idCompany", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "Office_idOffice", nullable = false)
    private Office office;
}
