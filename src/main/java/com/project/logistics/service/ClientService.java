package com.project.logistics.service;

import com.project.logistics.dto.ClientDto;
import com.project.logistics.entity.Client;
import com.project.logistics.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private ClientDto mapToDto(Client client) {
        var user = client.getUser();
        return ClientDto.builder()
                .id(client.getIdClient())
                .userId(user != null ? user.getIdUser() : null)
                .username(user != null ? user.getUsername() : null)
                .firstName(user != null ? user.getFirstName() : null)
                .lastName(user != null ? user.getLastName() : null)
                .email(user != null ? user.getEmail() : null)
                .build();
    }
}
