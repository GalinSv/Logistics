package com.project.logistics.controller;

import com.project.logistics.dto.ClientDto;
import com.project.logistics.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public List<ClientDto> getAllClients() {
        return clientService.getAllClients();
    }
}
