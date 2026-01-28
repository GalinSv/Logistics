package com.project.logistics.data.repository;

import com.project.logistics.data.entity.Employee;
import com.project.logistics.dto.EmployeeReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByUser_IdUser(Integer idUser);
    List<Employee> findAll();

    @Query("""
            SELECT new com.project.logistics.dto.EmployeeReportDto(
                e.idEmployee,
                u.firstName,
                u.lastName,
                u.phone,
                u.email,
                o.address,
                e.typeOfEmpl
            )
            FROM Employee e
            JOIN e.user u
            JOIN e.office o
            """)
    List<EmployeeReportDto> findAllForReport();

}
