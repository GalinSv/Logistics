package com.project.logistics.controller;

import com.project.logistics.data.entity.Office;
import com.project.logistics.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;

    @GetMapping
    public List<Office> getAllOffices() {
        return officeService.getAll();
    }

    @PostMapping
    public Office createOffice(@RequestBody Office office) {
        return officeService.create(office);
    }
}
