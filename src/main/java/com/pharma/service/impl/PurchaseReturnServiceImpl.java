package com.pharma.service.impl;

import com.pharma.dto.PurchaseReturnDto;
import com.pharma.entity.*;
import com.pharma.mapper.PurchaseReturnMapper;
import com.pharma.repository.InventoryDetailsRepository;
import com.pharma.repository.InventoryRepository;
import com.pharma.repository.PurchaseReturnRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.PurchaseReturnService;
import com.pharma.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Autowired
    private InventoryDetailsRepository inventoryDetailsRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public PurchaseReturnDto savePurchaseReturn(PurchaseReturnDto purchaseReturnDto, User user) {
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = user.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(purchaseReturnDto.getPharmacyId()));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        PurchaseReturnEntity purchaseReturnEntity = purchaseReturnMapper.toEntity(purchaseReturnDto);
        purchaseReturnEntity.setReturnId(UUID.randomUUID());
        purchaseReturnEntity.setCreatedBy(user.getId());
        purchaseReturnEntity.setCreatedDate(LocalDate.now());

        purchaseReturnEntity.setPharmacyId(purchaseReturnDto.getPharmacyId());

        String newReturnId1 = generateReturnId1();
        purchaseReturnEntity.setReturnId1(newReturnId1);

        if (purchaseReturnEntity.getPurchaseReturnItemEntities() != null) {
            for (PurchaseReturnItemEntity item : purchaseReturnEntity.getPurchaseReturnItemEntities()) {
                item.setReturnItemId(UUID.randomUUID());
                item.setCreatedBy(user.getId());
                item.setCreatedDate(LocalDate.now());
                item.setPurchaseReturnEntity(purchaseReturnEntity);

                // 🔻 Deduct from InventoryEntity (total stock)
                Optional<InventoryEntity> inventoryOpt = inventoryRepository.findByItemId(item.getItemId());
                if (inventoryOpt.isPresent()) {
                    InventoryEntity inventory = inventoryOpt.get();
                    synchronized (this) {
                        long updatedQty = inventory.getPackageQuantity() - item.getReturnQuantity();
                        if (updatedQty < 0) {
                            throw new RuntimeException("Insufficient stock for item ID: " + item.getItemId());
                        }
                        inventory.setPackageQuantity(updatedQty);
                        inventory.setModifiedBy(user.getId());
                        inventory.setModifiedDate(LocalDate.now());
                        inventoryRepository.save(inventory);
                    }
                } else {
                    throw new RuntimeException("Inventory not found for item ID: " + item.getItemId());
                }

                // 🔻 Deduct from InventoryDetailsEntity (batch stock)
                Optional<InventoryDetailsEntity> detailsOpt =
                        inventoryDetailsRepository.findByItemIdAndBatchNo(item.getItemId(), item.getBatchNo());

                if (detailsOpt.isPresent()) {
                    InventoryDetailsEntity details = detailsOpt.get();
                    long updatedQty = details.getPackageQuantity() - item.getReturnQuantity();
                    if (updatedQty < 0) {
                        throw new RuntimeException("Insufficient batch stock for item ID: " + item.getItemId() +
                                ", Batch No: " + item.getBatchNo());
                    }
                    details.setPackageQuantity(updatedQty);
                    details.setModifiedBy(user.getId());
                    details.setModifiedDate(LocalDate.now());
                    inventoryDetailsRepository.save(details);
                } else {
                    throw new RuntimeException("Inventory details not found for item ID: " + item.getItemId() +
                            ", Batch No: " + item.getBatchNo());
                }
            }
        }

        PurchaseReturnEntity savedEntity = purchaseReturnRepository.save(purchaseReturnEntity);
        return purchaseReturnMapper.toDto(savedEntity);
    }



    @Transactional
    @Override
    public List<PurchaseReturnDto> getAllPurchaseReturn(Long pharmacyId, User user) {
        boolean isMember = user.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<PurchaseReturnEntity> purchaseReturns = purchaseReturnRepository.findAllByPharmacyId(pharmacyId);
        return purchaseReturns.stream()
                .map(purchaseReturnMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public PurchaseReturnDto getPurchaseReturnById(Long pharmacyId, UUID returnId, User user) {
        boolean isMember = user.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<PurchaseReturnEntity> purchaseReturnEntity = purchaseReturnRepository.findByReturnIdAndPharmacyId(returnId, pharmacyId);

        if (purchaseReturnEntity.isEmpty()) {
            throw new RuntimeException("Purchase return not found with ID: " + returnId + " for pharmacy ID: " + pharmacyId);
        }
        return purchaseReturnMapper.toDto(purchaseReturnEntity.get());
    }

    @Transactional
    @Override
    public void deletePurchaseReturnById(Long pharmacyId, UUID returnId, User user) {
        boolean isMember = user.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<PurchaseReturnEntity> purchaseReturnEntity = purchaseReturnRepository.findByReturnIdAndCreatedBy(returnId, pharmacyId);
        if (purchaseReturnEntity.isEmpty()) {
            throw new RuntimeException("Purchase return not found with ID: " + returnId + " for pharmacy ID: " + pharmacyId);
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

    @Transactional
    @Override
    public BigDecimal getSumReturnAmountBySupplier(UUID supplierId, Long pharmacyId, User user) {
        boolean isMember = user.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        return purchaseReturnRepository
                .sumReturnAmountBySupplierIdAndPharmacyId(supplierId, pharmacyId);
    }



//    @Transactional
//    @Override
//    public BigDecimal getSumReturnAmountBySupplierAndCreatedBy(UUID supplierId, Long createdBy) {
//        return purchaseReturnRepository
//                .sumReturnAmountBySupplierIdAndCreditNoteAndCreatedBy(supplierId, createdBy);
//    }

}
