package com.pharma.service.impl;

import com.pharma.dto.*;
import com.pharma.entity.InventoryDetailsEntity;
import com.pharma.entity.StockEditEntity;
import com.pharma.entity.User;
import com.pharma.mapper.InventoryDetailsMapper;
import com.pharma.repository.InventoryDetailsRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.InventoryDetailsService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InventoryDetailsServiceImpl implements InventoryDetailsService {

    @Autowired
    private InventoryDetailsRepository inventoryDetailsRepository;

    @Autowired
    private InventoryDetailsMapper inventoryDetailsMapper;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public List<InventoryDetailsDto> getAllInventoryDetails(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<InventoryDetailsEntity> inventoryDetailsEntities = inventoryDetailsRepository.findAllByPharmacyId(pharmacyId);
        return inventoryDetailsEntities.stream()
                .map(inventoryDetailsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<ExpiredStockDto> getCurrentYearStockWithSupplier(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        return inventoryDetailsRepository.findCurrentYearStockWithSupplier(pharmacyId);
    }

    @Transactional
    @Override
    public List<ExpiredStockView> getNextThreeMonthsStockWithSupplier(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }
        return inventoryDetailsRepository.findNextThreeMonthsStockWithSupplier(pharmacyId);
    }


    @Transactional
    @Override
    public InventoryDetailsDto saveInventoryDetails(InventoryDetailsDto inventoryDetailsDto, User user) {
        InventoryDetailsEntity inventoryEntity = inventoryDetailsRepository.findById(inventoryDetailsDto.getInvDetailsId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventoryEntity.setModifiedBy(user.getId());
        inventoryEntity.setModifiedDate(LocalDate.now());

        if (inventoryDetailsDto.getStockEditDtos() != null) {
            for (StockEditDto stockEditDto : inventoryDetailsDto.getStockEditDtos()) {
                StockEditEntity stockEditEntity = new StockEditEntity();
                stockEditEntity.setStockEditId(UUID.randomUUID());
                stockEditEntity.setStockEditedDate(LocalDate.now());
                stockEditEntity.setAdjustmentType(stockEditDto.getAdjustmentType());
                stockEditEntity.setUpdatedStockQuantity(stockEditDto.getUpdatedStockQuantity());
                stockEditEntity.setCreatedBy(user.getId());
                stockEditEntity.setCreatedDate(LocalDate.now());
                stockEditEntity.setModifiedBy(user.getId());
                stockEditEntity.setModifiedDate(LocalDate.now());
                stockEditEntity.setInventoryDetailsEntity(inventoryEntity);

                Long currentQty = (inventoryEntity.getPackageQuantity() == null ? 0 : inventoryEntity.getPackageQuantity());
                if ("ADD_STOCK".equalsIgnoreCase(stockEditDto.getAdjustmentType())) {
                    inventoryEntity.setPackageQuantity(currentQty + stockEditDto.getUpdatedStockQuantity());
                } else if ("REMOVE_STOCK".equalsIgnoreCase(stockEditDto.getAdjustmentType())) {
                    if (currentQty < stockEditDto.getUpdatedStockQuantity()) {
                        throw new RuntimeException("Cannot remove more stock than available");
                    }
                    inventoryEntity.setPackageQuantity(currentQty - stockEditDto.getUpdatedStockQuantity());
                }

                inventoryEntity.getStockEditEntities().add(stockEditEntity);
            }
        }

        InventoryDetailsEntity savedEntity = inventoryDetailsRepository.save(inventoryEntity);

        return inventoryDetailsMapper.toDto(savedEntity);
    }


    @Transactional
    @Override
    public List<InventoryView> getInventory(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }
        return inventoryDetailsRepository.getInventoryDetailsByPharmacy(pharmacyId);
    }
}
