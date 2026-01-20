package com.pharma.service.impl;

import com.pharma.dto.BillReturnDto;
import com.pharma.dto.BillReturnItemDto;
import com.pharma.dto.BillReturnListDto;
import com.pharma.entity.*;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.BillReturnMapper;
import com.pharma.repository.BillReturnRepository;
import com.pharma.repository.InventoryDetailsRepository;
import com.pharma.repository.InventoryRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.BillReturnService;
import com.pharma.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillReturnServiceImpl implements BillReturnService {

    @Autowired
    private BillReturnRepository billReturnRepository;

    @Autowired
    private BillReturnMapper billReturnMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryDetailsRepository inventoryDetailsRepository;

    @Transactional
    @Override
    public BillReturnDto createBillReturn(BillReturnDto billReturnDto, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(billReturnDto.getPharmacyId()));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        BillReturnEntity billReturnEntity = billReturnMapper.toEntity(billReturnDto);
        billReturnEntity.setBillReturnId(UUID.randomUUID());
        billReturnEntity.setCreatedBy(user.getId());
        billReturnEntity.setCreatedDate(LocalDate.now());

        billReturnEntity.setPharmacyId(billReturnDto.getPharmacyId());

        String newBillReturnId1 = generateBillReturnId1(billReturnDto.getPharmacyId());
        billReturnEntity.setBillReturnId1(newBillReturnId1);

        if (billReturnEntity.getBillReturnItemEntities() != null) {
            for (BillReturnItemEntity item : billReturnEntity.getBillReturnItemEntities()) {
                item.setBillReturnItemId(UUID.randomUUID());
                item.setCreatedBy(user.getId());
                item.setCreatedDate(LocalDate.now());
                item.setBillReturnEntity(billReturnEntity);

                Optional<InventoryEntity> inventoryOpt = inventoryRepository.findByItemId(item.getItemId());
                if (inventoryOpt.isPresent()) {
                    InventoryEntity inventory = inventoryOpt.get();
                    synchronized (this) {
                        Long updatedQty = inventory.getPackageQuantity() + item.getReturnQuantity();
                        inventory.setPackageQuantity(updatedQty);
                        inventory.setModifiedBy(user.getId());
                        inventory.setModifiedDate(LocalDate.now());
                        inventoryRepository.save(inventory);
                    }
                } else {
                    throw new RuntimeException("Inventory not found for item ID: " + item.getItemId());
                }

                Optional<InventoryDetailsEntity> detailsOpt = inventoryDetailsRepository
                        .findByItemIdAndBatchNo(item.getItemId(), item.getBatchNo());

                if (detailsOpt.isPresent()) {
                    InventoryDetailsEntity details = detailsOpt.get();
                    Long updatedQty = details.getPackageQuantity() + item.getReturnQuantity();
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

        BillReturnEntity savedEntity = billReturnRepository.save(billReturnEntity);
        return billReturnMapper.toDto(savedEntity);
    }


    @Transactional
    @Override
    public List<BillReturnDto> getAllBillReturn(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<BillReturnEntity> billReturnEntities = billReturnRepository.findAllByPharmacyId(pharmacyId);
        return billReturnEntities.stream()
                .map(billReturnMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BillReturnDto getBillReturnById(Long pharmacyId, UUID billReturnId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<BillReturnEntity> billReturnEntity = billReturnRepository.findByBillReturnIdAndPharmacyId(billReturnId, pharmacyId);

        if (billReturnEntity.isEmpty()) {
            throw new RuntimeException("Bill Return not found with ID: " + billReturnId + " for pharmacy ID: " + pharmacyId);
        }
        return billReturnMapper.toDto(billReturnEntity.get());
    }

    @Transactional
    @Override
    public void deleteBillReturn(Long pharmacyId, UUID billReturnId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<BillReturnEntity> billReturnEntity = billReturnRepository.findByBillReturnIdAndPharmacyId(billReturnId, pharmacyId);
        if (billReturnEntity.isEmpty()) {
            throw new RuntimeException("Bill Return not found with ID: " + billReturnId + " for pharmacy ID: " + pharmacyId);
        }
        billReturnRepository.delete(billReturnEntity.get());
    }

    @Transactional
    @Override
    public List<BillReturnListDto> getBillReturnListsByPharmacy(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        return billReturnRepository.fetchBillReturnListsByPharmacyId(pharmacyId);
    }

    @Transactional
    private String generateBillReturnId1(Long pharmacyId) {
        String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);

        Optional<BillReturnEntity> latestBillReturnOpt =
                billReturnRepository.findLatestBillReturnForYearAndPharmacy(
                        pharmacyId, yearPart
                );

        int nextSequence = 1;

        if (latestBillReturnOpt.isPresent()) {
            String lastBillReturnId1 = latestBillReturnOpt.get().getBillReturnId1();
            // Example: BILLRTN-25-09 or BILLRTN-25-123
            String[] parts = lastBillReturnId1.split("-");

            if (parts.length == 3) {
                try {
                    nextSequence = Integer.parseInt(parts[2]) + 1;
                } catch (NumberFormatException ignored) {
                    nextSequence = 1;
                }
            }
        }

        String sequencePart = (nextSequence < 10)
                ? "0" + nextSequence
                : String.valueOf(nextSequence);

        return "BILLRTN-" + yearPart + "-" + sequencePart;
    }

    @Override
    @Transactional
    public BillReturnDto updateBillReturn(Long pharmacyId, UUID billReturnId, BillReturnDto updatedReturn, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        BillReturnEntity existingReturn =
                billReturnRepository
                        .findByBillReturnIdAndPharmacyId(billReturnId, pharmacyId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Bill return not found")
                        );

        existingReturn.setSubTotal(updatedReturn.getSubTotal());
        existingReturn.setTotalGst(updatedReturn.getTotalGst());
        existingReturn.setGrandTotal(updatedReturn.getGrandTotal());
        existingReturn.setReturnReason(updatedReturn.getReturnReason());
        existingReturn.setModifiedBy(user.getId());
        existingReturn.setModifiedDate(LocalDate.now());

        Map<UUID, BillReturnItemEntity> existingItemMap =
                existingReturn.getBillReturnItemEntities()
                        .stream()
                        .collect(Collectors.toMap(
                                BillReturnItemEntity::getBillReturnItemId,
                                i -> i
                        ));

        for (BillReturnItemDto itemDto : updatedReturn.getBillReturnItemDtos()) {

            if (itemDto.getBillReturnItemId() == null) {
                throw new RuntimeException("BillReturnItemId is mandatory");
            }

            BillReturnItemEntity itemEntity =
                    existingItemMap.get(itemDto.getBillReturnItemId());

            if (itemEntity == null) {
                throw new RuntimeException("Bill return item not found");
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
            itemEntity.setNetTotal(itemDto.getNetTotal());
            itemEntity.setGstAmount(itemDto.getGstAmount());
            itemEntity.setGrossTotal(itemDto.getGrossTotal());

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

            Long newInventoryQty = inventory.getPackageQuantity() + delta;

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

            Long newBatchQty = details.getPackageQuantity() + delta;

            if (newBatchQty < 0) {
                throw new RuntimeException("Batch quantity cannot be negative");
            }

            details.setPackageQuantity(newBatchQty);
            details.setModifiedBy(user.getId());
            details.setModifiedDate(LocalDate.now());

            inventoryDetailsRepository.save(details);
        }

        BillReturnEntity saved =
                billReturnRepository.save(existingReturn);

        return billReturnMapper.toDto(saved);
    }

}
