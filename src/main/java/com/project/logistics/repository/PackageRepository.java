package com.project.logistics.repository;

import com.project.logistics.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageRepository extends JpaRepository<PackageEntity, Integer> {
    List<PackageEntity> findBySender_IdClient(Integer senderId);
}
