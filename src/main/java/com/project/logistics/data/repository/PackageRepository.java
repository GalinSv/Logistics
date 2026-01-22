package com.project.logistics.data.repository;

import com.project.logistics.data.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageRepository extends JpaRepository<PackageEntity, Integer> {
    List<PackageEntity> findBySender_IdClient(Integer senderId);
    List<PackageEntity> findByEmployee_IdEmployee(Integer employeeId);
    List<PackageEntity> findByDateOfDeliveryIsNull();
    List<PackageEntity> findByReceiver_IdClientAndDateOfDeliveryIsNotNull(Integer clientId);
    List<PackageEntity> findBySender_IdClientOrReceiver_IdClient(Integer senderId, Integer receiverId);

}
