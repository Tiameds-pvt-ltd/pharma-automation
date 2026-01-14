package com.pharma.service.impl;

import com.pharma.dto.PurchaseReturnDto;
import com.pharma.dto.PurchaseReturnItemDto;
import com.pharma.entity.*;
import com.pharma.exception.ResourceNotFoundException;
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
import java.util.*;
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
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
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

        String newReturnId1 = generateReturnId1(purchaseReturnDto.getPharmacyId());
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
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
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
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
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
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<PurchaseReturnEntity> purchaseReturnEntity = purchaseReturnRepository.findByReturnIdAndPharmacyId(returnId, pharmacyId);
        if (purchaseReturnEntity.isEmpty()) {
            throw new RuntimeException("Purchase return not found with ID: " + returnId + " for pharmacy ID: " + pharmacyId);
        }
        purchaseReturnRepository.delete(purchaseReturnEntity.get());
    }

    @Transactional
    private String generateReturnId1(Long pharmacyId) {

        // YY = last 2 digits of year
        String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);

        Optional<PurchaseReturnEntity> latestReturnOpt =
                purchaseReturnRepository.findLatestReturnForYearAndPharmacy(
                        pharmacyId, yearPart
                );

        int nextSequence = 1;

        if (latestReturnOpt.isPresent()) {
            String lastReturnId1 = latestReturnOpt.get().getReturnId1();
            // Example: RTN-25-09 or RTN-25-123
            String[] parts = lastReturnId1.split("-");

            if (parts.length == 3) {
                try {
                    nextSequence = Integer.parseInt(parts[2]) + 1;
                } catch (NumberFormatException ignored) {
                    nextSequence = 1;
                }
            }
        }

        // Pad only for 1–9
        String sequencePart = (nextSequence < 10)
                ? "0" + nextSequence
                : String.valueOf(nextSequence);

        return "RTN-" + yearPart + "-" + sequencePart;
    }


    @Transactional
    @Override
    public BigDecimal getSumReturnAmountBySupplier(UUID supplierId, Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        return purchaseReturnRepository
                .sumReturnAmountBySupplierIdAndPharmacyId(supplierId, pharmacyId);
    }


    @Override
    @Transactional
    public PurchaseReturnDto updatePurchaseReturn(Long pharmacyId, UUID returnId, PurchaseReturnDto updatedReturn, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        PurchaseReturnEntity existingReturn =
                purchaseReturnRepository
                        .findByReturnIdAndPharmacyId(returnId, pharmacyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Purchase return not found")
                        );

        existingReturn.setReturnDate(updatedReturn.getReturnDate());
        existingReturn.setTotalAmount(updatedReturn.getTotalAmount());
        existingReturn.setTotalGst(updatedReturn.getTotalGst());
        existingReturn.setReturnAmount(updatedReturn.getReturnAmount());
        existingReturn.setRemark(updatedReturn.getRemark());
        existingReturn.setCreditNote(updatedReturn.getCreditNote());

        existingReturn.setModifiedBy(user.getId());
        existingReturn.setModifiedDate(LocalDate.now());

        Map<UUID, PurchaseReturnItemEntity> existingItemMap =
                existingReturn.getPurchaseReturnItemEntities()
                        .stream()
                        .collect(Collectors.toMap(
                                PurchaseReturnItemEntity::getReturnItemId,
                                i -> i
                        ));

        for (PurchaseReturnItemDto itemDto : updatedReturn.getPurchaseReturnItemDtos()) {

            if (itemDto.getReturnItemId() == null) {
                throw new RuntimeException("ReturnItemId is mandatory for update");
            }

            PurchaseReturnItemEntity itemEntity =
                    existingItemMap.get(itemDto.getReturnItemId());

            if (itemEntity == null) {
                throw new RuntimeException("Purchase return item not found");
            }

            if (!Objects.equals(itemEntity.getItemId(), itemDto.getItemId())) {
                throw new RuntimeException("Item change not allowed");
            }

            if (!Objects.equals(itemEntity.getBatchNo(), itemDto.getBatchNo())) {
                throw new RuntimeException("Batch change not allowed");
            }

            Long oldQty = itemEntity.getReturnQuantity();
            Long newQty = itemDto.getReturnQuantity();
            Long delta = newQty - oldQty;

            if (delta == 0) {
                continue;
            }

            itemEntity.setReturnQuantity(newQty);
            itemEntity.setReturnType(itemDto.getReturnType());
            itemEntity.setGstPercentage(itemDto.getGstPercentage());
            itemEntity.setDiscrepancyIn(itemDto.getDiscrepancyIn());
            itemEntity.setDiscrepancy(itemDto.getDiscrepancy());

            itemEntity.setModifiedBy(user.getId());
            itemEntity.setModifiedDate(LocalDate.now());

            InventoryEntity inventory =
                    inventoryRepository
                            .findByItemIdAndPharmacyId(
                                    itemEntity.getItemId(),
                                    pharmacyId
                            )
                            .orElseThrow(() ->
                                    new RuntimeException("Inventory not found")
                            );

            Long newInventoryQty = inventory.getPackageQuantity() - delta;

            if (newInventoryQty < 0) {
                throw new RuntimeException("Inventory cannot be negative");
            }

            inventory.setPackageQuantity(newInventoryQty);
            inventory.setModifiedBy(user.getId());
            inventory.setModifiedDate(LocalDate.now());

            inventoryRepository.save(inventory);

            InventoryDetailsEntity details =
                    inventoryDetailsRepository
                            .findByItemIdAndBatchNoAndPharmacyId(
                                    itemEntity.getItemId(),
                                    itemEntity.getBatchNo(),
                                    pharmacyId
                            )
                            .orElseThrow(() ->
                                    new RuntimeException("Inventory batch not found")
                            );

            Long newBatchQty = details.getPackageQuantity() - delta;

            if (newBatchQty < 0) {
                throw new RuntimeException("Batch quantity cannot be negative");
            }

            details.setPackageQuantity(newBatchQty);
            details.setModifiedBy(user.getId());
            details.setModifiedDate(LocalDate.now());

            inventoryDetailsRepository.save(details);
        }

        PurchaseReturnEntity saved =
                purchaseReturnRepository.save(existingReturn);

        return purchaseReturnMapper.toDto(saved);
    }
}
