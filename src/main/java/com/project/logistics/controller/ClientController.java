package com.project.logistics.controller;

import com.project.logistics.dto.ClientCreateDto;
import com.project.logistics.dto.ClientDto;
import com.project.logistics.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

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
}