package com.project.logistics.controller;

import com.project.logistics.data.entity.Office;
import com.project.logistics.dto.OfficeCreateDto;
import com.project.logistics.dto.OfficeDto;
import com.project.logistics.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;

    @PostMapping
    public OfficeDto create(@RequestBody OfficeCreateDto dto) {
        return officeService.create(dto);
    }

    @GetMapping("/{id}")
    public OfficeDto get(@PathVariable Integer id) {
        return officeService.getById(id);
    }

    @GetMapping
    public List<OfficeDto> getAll() {
        return officeService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        officeService.delete(id);
    }
}