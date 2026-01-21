package com.project.logistics.data.repository;

import com.project.logistics.data.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfficeRepository extends JpaRepository<Office, Integer> {

    List<Office> findByCompany_IdCompany(Integer idCompany);
}