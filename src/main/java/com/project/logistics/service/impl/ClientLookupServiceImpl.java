package com.project.logistics.service.impl;

import com.project.logistics.data.entity.Client;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.repository.ClientRepository;
import com.project.logistics.data.repository.UserRepository;
import com.project.logistics.dto.ClientByPhoneDto;
import com.project.logistics.service.ClientLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientLookupServiceImpl implements ClientLookupService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Override
    public ClientByPhoneDto getByPhone(String phone) {

        User user = userRepository.findByPhone(phone).orElse(null);

        if (user == null) {
            return ClientByPhoneDto.builder()
                    .exists(false)
                    .phone(phone)
                    .build();
        }

        Client client = clientRepository.findByUser_IdUser(user.getIdUser()).orElse(null);

        return ClientByPhoneDto.builder()
                .exists(true)
                .clientId(client != null ? client.getIdClient() : null)
                .phone(user.getPhone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
