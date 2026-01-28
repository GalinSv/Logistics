package com.project.logistics.data.repository;

import com.project.logistics.data.entity.Package;
import com.project.logistics.dto.PackageReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackageRepository extends JpaRepository<Package, Integer> {
    List<Package> findBySender_IdClient(Integer senderId);
    List<Package> findByEmployee_IdEmployee(Integer idEmployee);
    List<Package> findByDateOfDeliveryIsNull();
    List<Package> findByReceiver_IdClientAndDateOfDeliveryIsNotNull(Integer clientId);
    List<Package> findByReceiver_IdClient(Integer idClient);
    @Query("""
        SELECT new com.project.logistics.dto.PackageReportDto(
            p.idPackage,              
            sender.phone,             
            receiver.phone,           
            employeeUser.phone,      
            p.weight,                
            pricing.price,           
            p.toOffice,               
            p.addressToGo,          
            office.address,          
            p.dateOfDelivery,         
            p.status                  
        )
        FROM Package p
        JOIN p.sender s
        JOIN s.user sender
        JOIN p.receiver r
        JOIN r.user receiver
        JOIN p.employee e
        JOIN e.user employeeUser
        JOIN p.pricing pricing
        LEFT JOIN p.office office
    """)
    List<PackageReportDto> findAllForReport();
}



