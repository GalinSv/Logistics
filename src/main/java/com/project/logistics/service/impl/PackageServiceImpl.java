package com.project.logistics.service.impl;

import com.project.logistics.data.entity.Client;
import com.project.logistics.data.entity.Company;
import com.project.logistics.data.entity.Employee;
import com.project.logistics.data.entity.Office;
import com.project.logistics.data.entity.Pricing;
import com.project.logistics.data.entity.User;
import com.project.logistics.data.entity.PackageStatus;
import com.project.logistics.data.repository.*;
import com.project.logistics.dto.PackageCreateDto;
import com.project.logistics.dto.PackageDto;
import com.project.logistics.dto.PackageReportDto;
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
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final OfficeRepository officeRepository;
    private final CompanyRepository companyRepository;
    private final PricingService pricingService;

    @Override
    public PackageDto create(PackageCreateDto dto) {

        Company company = companyRepository.findById(1).orElseThrow();

        User senderUser = userRepository.findByPhone(dto.getSenderPhone()).orElse(null);
        if (senderUser == null) {
            senderUser = userRepository.save(
                    User.builder()
                            .phone(dto.getSenderPhone())
                            .username(dto.getSenderPhone())
                            .password("temp")
                            .firstName(dto.getSenderFirstName())
                            .lastName(dto.getSenderLastName())
                            .email(dto.getSenderPhone() + "@auto.local")
                            .company(company)
                            .build()
            );
        }

        Client sender = clientRepository.findByUser_IdUser(senderUser.getIdUser()).orElse(null);
        if (sender == null) {
            sender = clientRepository.save(Client.builder().user(senderUser).build());
        }

        User receiverUser = userRepository.findByPhone(dto.getReceiverPhone()).orElse(null);
        if (receiverUser == null) {
            receiverUser = userRepository.save(
                    User.builder()
                            .phone(dto.getReceiverPhone())
                            .username(dto.getReceiverPhone())
                            .password("temp")
                            .firstName(dto.getReceiverFirstName())
                            .lastName(dto.getReceiverLastName())
                            .email(dto.getReceiverPhone() + "@auto.local")
                            .company(company)
                            .build()
            );
        }

        Client receiver = clientRepository.findByUser_IdUser(receiverUser.getIdUser()).orElse(null);
        if (receiver == null) {
            receiver = clientRepository.save(Client.builder().user(receiverUser).build());
        }

        Employee employee = employeeRepository.findById(dto.getIdEmployee())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Pricing pricing = pricingService.createPricing(dto.getWeight(), dto.getToOffice());

        com.project.logistics.data.entity.Package pkg =
                com.project.logistics.data.entity.Package.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .employee(employee)
                        .weight(dto.getWeight())
                        .toOffice(dto.getToOffice())
                        .pricing(pricing)
                        .status(PackageStatus.CREATED)
                        .build();

        if (Boolean.TRUE.equals(dto.getToOffice())) {
            Office office = officeRepository.findById(dto.getOfficeId())
                    .orElseThrow(() -> new RuntimeException("Office not found"));
            pkg.setOffice(office);
            pkg.setAddressToGo(null);
        } else {
            pkg.setOffice(null);
            pkg.setAddressToGo(dto.getAddressToGo());
        }

        return mapToDto(packageRepository.save(pkg));
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
    public List<PackageDto> getUndelivered() {
        return packageRepository.findByDateOfDeliveryIsNull()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public PackageDto markDelivered(Integer idPackage) {
        com.project.logistics.data.entity.Package pkg =
                packageRepository.findById(idPackage)
                        .orElseThrow(() -> new RuntimeException("Package not found"));

        pkg.setDateOfDelivery(LocalDate.now());
        return mapToDto(packageRepository.save(pkg));
    }

    @Override
    public void delete(Integer idPackage) {
        packageRepository.deleteById(idPackage);
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
    public List<PackageReportDto> getReport() {
        return packageRepository.findAllForReport();
    }

    private PackageDto mapToDto(com.project.logistics.data.entity.Package pkg) {
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
                .officeId(pkg.getOffice() != null ? pkg.getOffice().getIdOffice() : null)
                .officeAddress(pkg.getOffice() != null ? pkg.getOffice().getAddress() : null)
                .build();
    }
}
