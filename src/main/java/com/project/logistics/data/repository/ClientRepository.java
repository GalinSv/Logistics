package com.project.logistics.data.repository;

import com.project.logistics.data.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByUser_IdUser(Integer idUser);
}
