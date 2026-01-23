package com.project.logistics.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idClient")
    private Integer idClient;

    @OneToOne
    @JoinColumn(name = "User_idUser", nullable = false)
    private User user;

    @OneToMany(mappedBy = "sender")
    private List<Package> sentPackages;

    @OneToMany(mappedBy = "receiver")
    private List<Package> receivedPackages;
}
