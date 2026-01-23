package com.project.logistics.service.impl;

import com.project.logistics.data.entity.Package;
import com.project.logistics.dto.PackageCreateDto;
import com.project.logistics.dto.PackageDto;
import com.project.logistics.data.entity.*;
import com.project.logistics.data.repository.*;
import com.project.logistics.exception.NotFoundException;
import com.project.logistics.service.PackageService;
import com.project.logistics.service.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final PricingService pricingService;

    @Override
    public PackageDto create(PackageCreateDto dto) {
        Client sender = clientRepository.findById(dto.getIdSenderClient())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Client receiver = clientRepository.findById(dto.getIdReceiverClient())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Employee employee = employeeRepository.findById(dto.getIdEmployee())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Pricing pricing = pricingService.createPricing(dto.getWeight(), dto.getToOffice());

        Package pkg = Package.builder()
                .sender(sender)
                .receiver(receiver)
                .employee(employee)
                .weight(dto.getWeight())
                .addressToGo(dto.getAddressToGo())
                .toOffice(dto.getToOffice())
                .pricing(pricing)
                .status(PackageStatus.CREATED)
                .build();

        Package saved = packageRepository.save(pkg);
        return mapToDto(saved);
    }

    @Override
    public PackageDto getById(Integer idPackage) {
        return packageRepository.findById(idPackage)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Package not found"));
    }

    @Override
    public List<PackageDto> getAll() {
        return packageRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<PackageDto> getByEmployee(Integer idEmployee) {
        return packageRepository.findByEmployee_IdEmployee(idEmployee)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<PackageDto> getSentByClient(Integer idClient) {
        return packageRepository.findBySender_IdClient(idClient)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<PackageDto> getReceivedByClient(Integer idClient) {
        return packageRepository.findByReceiver_IdClient(idClient)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<PackageDto> getUndelivered() {
        return packageRepository.findByDateOfDeliveryIsNull()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public PackageDto markDelivered(Integer idPackage) {
        Package pkg = packageRepository.findById(idPackage)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        pkg.setDateOfDelivery(LocalDate.now());
        return mapToDto(packageRepository.save(pkg));
    }

    @Override
    public void delete(Integer idPackage) {
        if (!packageRepository.existsById(idPackage)) {
            throw new RuntimeException("Package not found");
        }
        packageRepository.deleteById(idPackage);
    }

    private PackageDto mapToDto(Package pkg) {
        return PackageDto.builder()
                .idPackage(pkg.getIdPackage())
                .idSenderClient(pkg.getSender().getIdClient())
                .idReceiverClient(pkg.getReceiver().getIdClient())
                .idEmployee(pkg.getEmployee().getIdEmployee())
                .weight(pkg.getWeight())
                .addressToGo(pkg.getAddressToGo())
                .toOffice(pkg.getToOffice())
                .dateOfDelivery(pkg.getDateOfDelivery())
                .price(pkg.getPricing().getPrice())
                .build();
    }
}


