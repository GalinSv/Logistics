package com.project.logistics.service.impl;

import com.project.logistics.dto.PackageCreateDto;
import com.project.logistics.dto.PackageDto;
import com.project.logistics.data.entity.*;
import com.project.logistics.data.repository.*;
import com.project.logistics.exception.NotFoundException;
import com.project.logistics.service.PackageService;
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
    private final PricingRepository pricingRepository;

    @Override
    public List<PackageDto> getAllPackages() {
        return packageRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<PackageDto> getPackagesBySender(Integer senderId) {
        return packageRepository.findBySender_IdClient(senderId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public PackageDto createPackage(PackageCreateDto dto) {

        Client sender = clientRepository.findById(dto.getSenderClientId())
                .orElseThrow(() -> new NotFoundException("Sender not found: " + dto.getSenderClientId()));

        Client receiver = clientRepository.findById(dto.getReceiverClientId())
                .orElseThrow(() -> new NotFoundException("Receiver not found: " + dto.getReceiverClientId()));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Employee not found: " + dto.getEmployeeId()));

        Pricing pricing = pricingRepository.findById(dto.getPricingId())
                .orElseThrow(() -> new NotFoundException("Pricing not found: " + dto.getPricingId()));

        PackageEntity pkg = PackageEntity.builder()
                .weight(dto.getWeight())
                .addressToGo(dto.getDeliveryAddress())
                .toOffice(dto.getToOffice())
                .sender(sender)
                .receiver(receiver)
                .employee(employee)
                .pricing(pricing)
                .build();

        return mapToDto(packageRepository.save(pkg));
    }

    @Override
    public void deletePackage(Integer id) {
        if (!packageRepository.existsById(id)) {
            throw new NotFoundException("Package not found: " + id);
        }
        packageRepository.deleteById(id);
    }

    @Override
    public double calculatePackagePrice(Integer packageId) {
        PackageEntity pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new NotFoundException("Package not found: " + packageId));

        return calculatePackagePrice(pkg);
    }

    @Override
    public List<PackageDto> getPackagesByEmployee(Integer employeeId) {
        return packageRepository.findByEmployee_IdEmployee(employeeId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<PackageDto> getUndeliveredPackages() {
        return packageRepository.findByDateOfDeliveryIsNull()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<PackageDto> getReceivedByClient(Integer clientId) {
        return packageRepository
                .findByReceiver_IdClientAndDateOfDeliveryIsNotNull(clientId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public double getRevenue(LocalDate from, LocalDate to) {
        return packageRepository.findAll().stream()
                .filter(p -> p.getDateOfDelivery() != null)
                .filter(p -> !p.getDateOfDelivery().isBefore(from)
                        && !p.getDateOfDelivery().isAfter(to))
                .mapToDouble(this::calculatePackagePrice)
                .sum();
    }

    @Override
    public double getRevenueByEmployee(Integer employeeId) {
        return packageRepository.findByEmployee_IdEmployee(employeeId).stream()
                .filter(p -> p.getDateOfDelivery() != null)
                .mapToDouble(this::calculatePackagePrice)
                .sum();
    }

    @Override
    public List<PackageDto> getPackagesForUser(boolean isEmployee, Integer clientId) {
        if (isEmployee) {
            return getAllPackages();
        }

        List<PackageDto> sent = getPackagesBySender(clientId);
        List<PackageDto> received = getReceivedByClient(clientId);

        return java.util.stream.Stream
                .concat(sent.stream(), received.stream())
                .distinct()
                .toList();
    }

    /* ===================== PRIVATE HELPERS ===================== */

    private PackageDto mapToDto(PackageEntity p) {

        var sender = p.getSender();
        var receiver = p.getReceiver();
        var employee = p.getEmployee();
        var pricing = p.getPricing();

        return PackageDto.builder()
                .id(p.getIdPackage())
                .weight(p.getWeight())
                .addressToGo(p.getAddressToGo())
                .dateOfDelivery(p.getDateOfDelivery())
                .toOffice(p.getToOffice())
                .senderId(sender != null ? sender.getIdClient() : null)
                .senderUsername(sender != null && sender.getUser() != null ? sender.getUser().getUsername() : null)
                .receiverId(receiver != null ? receiver.getIdClient() : null)
                .receiverUsername(receiver != null && receiver.getUser() != null ? receiver.getUser().getUsername() : null)
                .employeeId(employee != null ? employee.getIdEmployee() : null)
                .employeeUsername(employee != null && employee.getUser() != null ? employee.getUser().getUsername() : null)
                .pricingId(pricing != null ? pricing.getIdTransactions() : null)
                .pricePerKilogram(pricing != null ? pricing.getPricePerKilogram() : null)
                .pricingToAddress(pricing != null ? pricing.getToAddress() : null)
                .build();
    }

    private double calculatePackagePrice(PackageEntity p) {
        double basePrice = p.getWeight() * p.getPricing().getPricePerKilogram();

        if (!p.getToOffice()) {
            basePrice += p.getPricing().getToAddress();
        }

        return basePrice;
    }
}
