package com.pharma.service.impl;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.InventoryDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.InventoryEntity;
import com.pharma.entity.User;
import com.pharma.mapper.InventoryMapper;
import com.pharma.repository.InventoryRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public List<InventoryDto> getAllInventory(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<InventoryEntity> inventoryEntities = inventoryRepository.findAllByPharmacyId(pharmacyId);
        return inventoryEntities.stream()
                .map(inventoryMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<StockItemDto> getExpiredStock(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }
        List<Object[]> results = inventoryRepository.getExpiredStockByPharmacy(pharmacyId);

        return results.stream()
                .map(obj -> {
                    StockItemDto dto = new StockItemDto();
                    dto.setItemId((UUID) obj[0]);
                    dto.setPackageQuantity(((Number) obj[1]).longValue());
                    dto.setPharmacyId(pharmacyId);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public List<ExpiredStockDto> getExpiredStockWithSupplier(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        return inventoryRepository.findExpiredStockWithSupplier(pharmacyId);
    }

}
