package com.project.logistics.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "office")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOffice")
    private Integer idOffice;

    @Column(name = "address", nullable = false, columnDefinition = "LONGTEXT")
    private String address;

    @ManyToOne
    @JoinColumn(name = "Company_idCompany", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "office")
    private List<Employee> employees;
}
