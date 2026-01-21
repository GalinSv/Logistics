package com.project.logistics.service.impl;

import com.project.logistics.data.entity.Office;
import com.project.logistics.data.repository.OfficeRepository;
import com.project.logistics.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;

    @Override
    public List<Office> getAll() {
        return officeRepository.findAll();
    }

    @Override
    public Office create(Office office) {
        return officeRepository.save(office);
    }
}
