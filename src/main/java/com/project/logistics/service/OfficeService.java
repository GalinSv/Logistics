package com.project.logistics.service;

import com.project.logistics.data.entity.Office;
import com.project.logistics.data.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;

    public List<Office> getAll() {
        return officeRepository.findAll();
    }

    public Office create(Office office) {
        return officeRepository.save(office);
    }
}
