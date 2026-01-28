package com.project.logistics.data.repository;

import com.project.logistics.data.entity.Client;
import com.project.logistics.dto.ClientReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByUser_IdUser(Integer idUser);
    @Query("""
        SELECT new com.project.logistics.dto.ClientReportDto(
            c.idClient,
            u.firstName,
            u.lastName,
            u.phone,
            u.email
        )
        FROM Client c
        JOIN c.user u
        """)
    List<ClientReportDto> findAllForReport();

}
