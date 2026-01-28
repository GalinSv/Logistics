package com.project.logistics.controller;

import com.project.logistics.dto.ClientByPhoneDto;
import com.project.logistics.dto.ClientCreateDto;
import com.project.logistics.dto.ClientDto;
import com.project.logistics.service.ClientLookupService;
import com.project.logistics.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientLookupService clientLookupService;

    @PostMapping
    public ClientDto create(@RequestBody ClientCreateDto dto) {
        return clientService.create(dto);
    }

    @GetMapping("/{id}")
    public ClientDto get(@PathVariable Integer id) {
        return clientService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        clientService.delete(id);
    }

    @GetMapping("/by-phone")
    public ClientByPhoneDto getByPhone(@RequestParam String phone) {
        return clientLookupService.getByPhone(phone);
    }
}
