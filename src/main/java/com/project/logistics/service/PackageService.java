package com.project.logistics.service;

import com.project.logistics.data.entity.*;
import com.project.logistics.data.repository.*;
import com.project.logistics.dto.PackageCreateDto;
import com.project.logistics.dto.PackageDto;
import com.project.logistics.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final PricingRepository pricingRepository;
    private final OfficeRepository officeRepository;

    public List<PackageDto> getAllPackages() {
        return packageRepository.findAll().stream().map(this::mapToDto).toList();
    }

    private PackageDto mapToDto(PackageEntity p) {
        var sender = p.getSender();
        var receiver = p.getReceiver();
        var employee = p.getEmployee();
        var pricing = p.getPricing();
        var office = p.getOffice();

        var senderUser = sender != null ? sender.getUser() : null;
        var receiverUser = receiver != null ? receiver.getUser() : null;

        String senderFullName =
                senderUser != null ? senderUser.getFirstName() + " " + senderUser.getLastName() : null;

        String receiverFullName =
                receiverUser != null ? receiverUser.getFirstName() + " " + receiverUser.getLastName() : null;

        String employeeFullName =
                employee != null && employee.getUser() != null
                        ? employee.getUser().getFirstName() + " " + employee.getUser().getLastName()
                        : null;

        return PackageDto.builder()
                .id(p.getIdPackage())
                .weight(p.getWeight())
                .addressToGo(p.getAddressToGo())
                .dateOfDelivery(p.getDateOfDelivery())
                .toOffice(p.getToOffice())

                .senderId(sender != null ? sender.getIdClient() : null)
                .senderUsername(senderUser != null ? senderUser.getUsername() : null)

                .receiverId(receiver != null ? receiver.getIdClient() : null)
                .receiverUsername(receiverUser != null ? receiverUser.getUsername() : null)

                .employeeId(employee != null ? employee.getIdEmployee() : null)
                .employeeUsername(employee != null && employee.getUser() != null ? employee.getUser().getUsername() : null)
                .employeeFullName(employeeFullName)

                .pricingId(pricing != null ? pricing.getIdTransactions() : null)
                .pricePerKilogram(pricing != null ? pricing.getPricePerKilogram() : null)
                .pricingToAddress(p.getToOffice() != null ? (p.getToOffice() ? 0 : 1) : null)


                .status(p.getStatus() == null ? "CREATED" : p.getStatus().name())

                .senderFullName(senderFullName)
                .receiverFullName(receiverFullName)

                .officeId(office != null ? office.getIdOffice() : null)
                .officeAddress(office != null ? office.getAddress() : null)

                .build();
    }

    public List<PackageDto> getPackagesBySender(Integer senderId) {
        return packageRepository.findBySender_IdClient(senderId).stream().map(this::mapToDto).toList();
    }

    public PackageDto createPackage(PackageCreateDto dto) {
        Client sender = clientRepository.findById(dto.getSenderClientId())
                .orElseThrow(() -> new NotFoundException("Sender not found: " + dto.getSenderClientId()));

        Client receiver = clientRepository.findById(dto.getReceiverClientId())
                .orElseThrow(() -> new NotFoundException("Receiver not found: " + dto.getReceiverClientId()));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new NotFoundException("Employee not found: " + dto.getEmployeeId()));

        Pricing pricing = pricingRepository.findById(dto.getPricingId())
                .orElseThrow(() -> new NotFoundException("Pricing not found: " + dto.getPricingId()));

        Office office = null;
        if (Boolean.TRUE.equals(dto.getToOffice())) {
            if (dto.getOfficeId() == null) {
                throw new IllegalArgumentException("officeId is required when toOffice = true");
            }
            office = officeRepository.findById(dto.getOfficeId())
                    .orElseThrow(() -> new NotFoundException("Office not found: " + dto.getOfficeId()));
        }

        String addressToGo = Boolean.TRUE.equals(dto.getToOffice()) ? null : dto.getDeliveryAddress();

        PackageEntity pkg = PackageEntity.builder()
                .weight(dto.getWeight())
                .toOffice(dto.getToOffice())
                .addressToGo(addressToGo)
                .sender(sender)
                .receiver(receiver)
                .employee(employee)
                .pricing(pricing)
                .office(office)
                .status(PackageStatus.CREATED)
                .build();

        return mapToDto(packageRepository.save(pkg));
    }

    public void deletePackage(Integer id) {
        if (!packageRepository.existsById(id)) throw new NotFoundException("Package not found: " + id);
        packageRepository.deleteById(id);
    }

    public double calculatePackagePrice(Integer packageId) {
        PackageEntity pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new NotFoundException("Package not found: " + packageId));
        return calculatePackagePrice(pkg);
    }

    private double calculatePackagePrice(PackageEntity p) {
        double basePrice = p.getWeight() * p.getPricing().getPricePerKilogram();
        if (Boolean.FALSE.equals(p.getToOffice())) basePrice += 1;
        return basePrice;
    }

    public List<PackageDto> getUndeliveredPackages() {
        return packageRepository.findByDateOfDeliveryIsNull().stream().map(this::mapToDto).toList();
    }

    public List<PackageDto> getReceivedByClient(Integer clientId) {
        return packageRepository.findByReceiver_IdClientAndDateOfDeliveryIsNotNull(clientId)
                .stream().map(this::mapToDto).toList();
    }

    public double getRevenue(LocalDate from, LocalDate to) {
        return packageRepository.findAll().stream()
                .filter(p -> p.getDateOfDelivery() != null)
                .filter(p -> !p.getDateOfDelivery().isBefore(from) && !p.getDateOfDelivery().isAfter(to))
                .mapToDouble(this::calculatePackagePrice)
                .sum();
    }

    @Transactional
    public PackageDto updateStatus(Integer id, String status) {
        PackageEntity pkg = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        PackageStatus newStatus = PackageStatus.valueOf(status.toUpperCase());

        if (pkg.getStatus() == PackageStatus.CANCELLED || pkg.getStatus() == PackageStatus.DELIVERED) {
            throw new RuntimeException("Cannot change status of DELIVERED/CANCELLED package");
        }

        pkg.setStatus(newStatus);
        if (newStatus == PackageStatus.DELIVERED) pkg.setDateOfDelivery(LocalDate.now());

        return mapToDto(packageRepository.save(pkg));
    }

    public List<PackageDto> getAllPackagesForClient(Integer clientId) {
        return packageRepository.findBySender_IdClientOrReceiver_IdClient(clientId, clientId)
                .stream().map(this::mapToDto).toList();
    }
}
