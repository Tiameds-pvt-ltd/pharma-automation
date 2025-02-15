package com.pharma.service.impl;

import com.pharma.dto.InventoryDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.InventoryEntity;
import com.pharma.mapper.InventoryMapper;
import com.pharma.repository.InventoryRepository;
import com.pharma.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private InventoryRepository inventoryRepository;

    @Override
    public List<InventoryDto> getAllInventory() {
        List<InventoryEntity> inventoryEntities = inventoryRepository.findAll();
        return inventoryEntities.stream().map((inventoryEntity) -> InventoryMapper.mapToInventoryDto(inventoryEntity)).collect(Collectors.toList());
    }

    @Override
    public List<StockItemDto> getExpiredStock() {
        List<Object[]> results = inventoryRepository.getExpiredStock();

        return results.stream()
                .map(obj -> {
                    StockItemDto dto = new StockItemDto();
                    dto.setItemId((String) obj[0]);    // Map itemId
                    dto.setPackageQuantity(((Number) obj[1]).intValue()); // Map expiredQuantity
                    return dto;
                })
                .collect(Collectors.toList());
    }


}
