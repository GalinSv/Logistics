package com.project.logistics.service;

import com.project.logistics.dto.PackageDto;
import com.project.logistics.data.entity.PackageEntity;
import com.project.logistics.data.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;

    public List<PackageDto> getAllPackages() {
        return packageRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

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
    public List<PackageDto> getPackagesBySender(Integer senderId) {
        return packageRepository.findBySender_IdClient(senderId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }
}
