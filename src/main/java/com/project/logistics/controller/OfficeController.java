package com.project.logistics.controller;

import com.project.logistics.dto.OfficeDto;
import com.project.logistics.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offices")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OfficeController {

    private final OfficeService officeService;

    @GetMapping
    public List<OfficeDto> getAll() {
        return officeService.getAll();
    }
}
