package com.project.logistics.entity;

import jakarta.persistence.*;
import lombok.*;

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

    // CHILD → owns the FK column User_idUser
    @OneToOne
    @JoinColumn(name = "User_idUser", nullable = false)
    private User user;
}
