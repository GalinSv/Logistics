package com.project.logistics.data.repository;

import com.project.logistics.data.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Integer> {
    List<Package> findBySender_IdClient(Integer senderId);
    List<Package> findByEmployee_IdEmployee(Integer idEmployee);
    List<Package> findByDateOfDeliveryIsNull();
    List<Package> findByReceiver_IdClientAndDateOfDeliveryIsNotNull(Integer clientId);
    List<Package> findByReceiver_IdClient(Integer idClient);



}
