package com.project.logistics.service;

import com.project.logistics.data.entity.Client;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.ClientRepository;
import com.project.logistics.data.repository.CompanyRepository;
import com.project.logistics.data.repository.UserRepository;
import com.project.logistics.dto.ClientCreateDto;
import com.project.logistics.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

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
                .phone(user != null ? user.getPhone() : null)
                .build();
    }

    public ClientDto lookupByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone is required");
        }

        String normalized = phone.trim();

        User user = userRepository.findByPhone(normalized)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No user with this phone"));

        Client client = clientRepository.findByUser_IdUser(user.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User is not a client"));

        return mapToDto(client);
    }

    public ClientDto createClient(ClientCreateDto dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body is required");
        }

        String phone = dto.getPhone() == null ? "" : dto.getPhone().trim();
        if (phone.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone is required");
        }

        if (dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()
                || dto.getLastName() == null || dto.getLastName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name and last name are required");
        }

        if (userRepository.findByPhone(phone).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone already exists");
        }

        var defaultCompany = companyRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Default company missing (id=1)"
                ));

        User user = User.builder()
                .username(phone) // временно
                .password("pass123")
                .firstName(dto.getFirstName().trim())
                .lastName(dto.getLastName().trim())
                .phone(phone)
                .email(phone + "@auto.local")
                .company(defaultCompany)
                .build();

        user = userRepository.save(user);

        Client client = Client.builder()
                .user(user)
                .build();

        client = clientRepository.save(client);

        return mapToDto(client);
    }
}
