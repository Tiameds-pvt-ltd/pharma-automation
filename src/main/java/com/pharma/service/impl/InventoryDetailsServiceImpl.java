package com.pharma.service.impl;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.ExpiredStockView;
import com.pharma.dto.InventoryDetailsDto;
import com.pharma.dto.StockEditDto;
import com.pharma.entity.InventoryDetailsEntity;
import com.pharma.entity.StockEditEntity;
import com.pharma.entity.User;
import com.pharma.mapper.InventoryDetailsMapper;
import com.pharma.repository.InventoryDetailsRepository;
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

    @Override
    public List<InventoryDetailsDto> getAllInventoryDetails(Long createdById) {
        List<InventoryDetailsEntity> inventoryDetailsEntities = inventoryDetailsRepository.findAllByCreatedBy(createdById);
        return inventoryDetailsEntities.stream()
                .map(inventoryDetailsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<ExpiredStockDto> getCurrentYearStockWithSupplier(Long createdById) {
        return inventoryDetailsRepository.findCurrentYearStockWithSupplier(createdById);
    }

    @Transactional
    @Override
    public List<ExpiredStockView> getNextThreeMonthsStockWithSupplier(Long createdById) {
        return inventoryDetailsRepository.findNextThreeMonthsStockWithSupplier(createdById);
    }

    @Transactional
    @Override
    public InventoryDetailsDto saveInventoryDetails(InventoryDetailsDto inventoryDetailsDto, User user) {
        // Always fetch existing inventory
        InventoryDetailsEntity inventoryEntity = inventoryDetailsRepository.findById(inventoryDetailsDto.getInvDetailsId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventoryEntity.setModifiedBy(user.getId());
        inventoryEntity.setModifiedDate(LocalDate.now());

        // Process adjustments from StockEditDtos
        if (inventoryDetailsDto.getStockEditDtos() != null) {
            for (StockEditDto stockEditDto : inventoryDetailsDto.getStockEditDtos()) {
                // Create a new StockEditEntity for each adjustment
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

                // Update inventory quantity
                Long currentQty = (inventoryEntity.getPackageQuantity() == null ? 0 : inventoryEntity.getPackageQuantity());
                if ("ADD_STOCK".equalsIgnoreCase(stockEditDto.getAdjustmentType())) {
                    inventoryEntity.setPackageQuantity(currentQty + stockEditDto.getUpdatedStockQuantity());
                } else if ("REMOVE_STOCK".equalsIgnoreCase(stockEditDto.getAdjustmentType())) {
                    if (currentQty < stockEditDto.getUpdatedStockQuantity()) {
                        throw new RuntimeException("Cannot remove more stock than available");
                    }
                    inventoryEntity.setPackageQuantity(currentQty - stockEditDto.getUpdatedStockQuantity());
                }

                // Always add a new row in pharma_stock_edit
                inventoryEntity.getStockEditEntities().add(stockEditEntity);
            }
        }

        // Save parent (cascades children)
        InventoryDetailsEntity savedEntity = inventoryDetailsRepository.save(inventoryEntity);

        return inventoryDetailsMapper.toDto(savedEntity);
    }
}
