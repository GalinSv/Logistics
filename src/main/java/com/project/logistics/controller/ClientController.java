package com.project.logistics.controller;

import com.project.logistics.dto.ClientCreateDto;
import com.project.logistics.dto.ClientDto;
import com.project.logistics.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/lookup")
    public ClientDto lookupByPhone(@RequestParam String phone) {
        return clientService.lookupByPhone(phone);
    }

    @PostMapping
    public ClientDto create(@RequestBody ClientCreateDto dto) {
        return clientService.createClient(dto);
    }
}
