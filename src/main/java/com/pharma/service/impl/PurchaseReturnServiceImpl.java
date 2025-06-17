package com.pharma.service.impl;

import com.pharma.dto.PurchaseReturnDto;
import com.pharma.entity.*;
import com.pharma.mapper.PurchaseReturnMapper;
import com.pharma.repository.PurchaseReturnRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.PurchaseReturnService;
import com.pharma.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PurchaseReturnServiceImpl implements PurchaseReturnService {

    @Autowired
    private PurchaseReturnRepository purchaseReturnRepository;

    @Autowired
    private PurchaseReturnMapper purchaseReturnMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PurchaseReturnDto savePurchaseReturn(PurchaseReturnDto purchaseReturnDto, User user) {
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PurchaseReturnEntity purchaseReturnEntity = purchaseReturnMapper.toEntity(purchaseReturnDto);
        purchaseReturnEntity.setReturnId(UUID.randomUUID());
        purchaseReturnEntity.setCreatedBy(user.getId());
        purchaseReturnEntity.setCreatedDate(LocalDate.now());

        String newReturnId1 = generateReturnId1();
        purchaseReturnEntity.setReturnId1(newReturnId1);

        if (purchaseReturnEntity.getPurchaseReturnItemEntities() != null) {
            for (PurchaseReturnItemEntity item : purchaseReturnEntity.getPurchaseReturnItemEntities()) {
                item.setReturnItemId(UUID.randomUUID());
                item.setCreatedBy(user.getId());
                item.setCreatedDate(LocalDate.now());
                item.setPurchaseReturnEntity(purchaseReturnEntity);
            }
        }

        PurchaseReturnEntity savedEntity = purchaseReturnRepository.save(purchaseReturnEntity);
        return purchaseReturnMapper.toDto(savedEntity);
    }

    @Override
    public List<PurchaseReturnDto> getAllPurchaseReturn(Long createdById) {
        List<PurchaseReturnEntity> purchaseReturns = purchaseReturnRepository.findAllByCreatedBy(createdById);
        return purchaseReturns.stream()
                .map(purchaseReturnMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseReturnDto getPurchaseReturnById(Long createdById, UUID returnId) {
        Optional<PurchaseReturnEntity> purchaseReturnEntity = purchaseReturnRepository.findByReturnIdAndCreatedBy(returnId, createdById);

        if (purchaseReturnEntity.isEmpty()) {
            throw new RuntimeException("Purchase return not found with ID: " + returnId + " for user ID: " + createdById);
        }
        return purchaseReturnMapper.toDto(purchaseReturnEntity.get());
    }

    @Override
    public void deletePurchaseReturnById(Long createdById, UUID returnId) {
        Optional<PurchaseReturnEntity> purchaseReturnEntity = purchaseReturnRepository.findByReturnIdAndCreatedBy(returnId, createdById);
        if (purchaseReturnEntity.isEmpty()) {
            throw new RuntimeException("Purchase return not found with ID: " + returnId + " for user ID: " + createdById);
        }
        purchaseReturnRepository.delete(purchaseReturnEntity.get());
    }

    private String generateReturnId1() {
        String yearPart = String.valueOf(LocalDate.now().getYear());

        Optional<PurchaseReturnEntity> latestReturnOpt = purchaseReturnRepository.findLatestReturnForYear(yearPart);

        int newSequence = 1;
        if (latestReturnOpt.isPresent()) {
            String lastReturnId1 = latestReturnOpt.get().getReturnId1();
            String[] parts = lastReturnId1.split("-");

            try {
                if (parts.length == 3) {
                    newSequence = Integer.parseInt(parts[2]) + 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing order sequence: " + lastReturnId1);
            }
        }

        return String.format("RTN-%s-%05d", yearPart, newSequence);
    }

//    private String generateReturnId1() {
//        LocalDate today = LocalDate.now();
//        String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//
//        Optional<PurchaseReturnEntity> latestReturnOpt = purchaseReturnRepository.findLatestReturnForToday(datePart);
//
//        int newSequence = 1;
//        if (latestReturnOpt.isPresent()) {
//            String lastReturnId1 = latestReturnOpt.get().getReturnId1();
//            String[] parts = lastReturnId1.split("-");
//
//            try {
//                if (parts.length == 3) {
//                    newSequence = Integer.parseInt(parts[2]) + 1;
//                }
//            } catch (NumberFormatException e) {
//                System.err.println("Error parsing return sequence: " + lastReturnId1);
//            }
//        }
//        return String.format("RTN-%s-%05d", datePart, newSequence);
//    }
}
