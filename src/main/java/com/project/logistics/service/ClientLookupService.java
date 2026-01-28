package com.project.logistics.service;

import com.project.logistics.dto.ClientByPhoneDto;

public interface ClientLookupService {
    ClientByPhoneDto getByPhone(String phone);
}
