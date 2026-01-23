package com.project.logistics.service.impl;

import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.PackageRepository;
import com.project.logistics.data.repository.UserRepository;
import com.project.logistics.dto.ClientCreateDto;
import com.project.logistics.dto.ClientDto;
import com.project.logistics.data.entity.Client;
import com.project.logistics.data.repository.ClientRepository;
import com.project.logistics.dto.PackageDto;
import com.project.logistics.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PackageRepository packageRepository;

    @Override
    public ClientDto create(ClientCreateDto dto) {
        User user = userRepository.findById(dto.getIdUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Client client = Client.builder()
                .user(user)
                .build();

        Client saved = clientRepository.save(client);
        return mapToDto(saved);
    }

    @Override
    public ClientDto getById(Integer idClient) {
        return clientRepository.findById(idClient)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @Override
    public ClientDto getByUser(Integer idUser) {
        return clientRepository.findByUser_IdUser(idUser)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }



    @Override
    public void delete(Integer idClient) {
        if (!clientRepository.existsById(idClient)) {
            throw new RuntimeException("Client not found");
        }
        clientRepository.deleteById(idClient);
    }

    private ClientDto mapToDto(Client client) {
        return ClientDto.builder()
                .idClient(client.getIdClient())
                .idUser(client.getUser().getIdUser())
                .build();
    }


}