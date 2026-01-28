package com.project.logistics.service;

import com.project.logistics.dto.ClientCreateDto;
import com.project.logistics.dto.ClientDto;
import com.project.logistics.dto.ClientReportDto;
import com.project.logistics.dto.PackageDto;

import java.util.List;

public interface ClientService {

    ClientDto create(ClientCreateDto dto);

    ClientDto getById(Integer idClient);

    ClientDto getByUser(Integer idUser);


    void delete(Integer idClient);
    List<ClientReportDto> getReport();
}