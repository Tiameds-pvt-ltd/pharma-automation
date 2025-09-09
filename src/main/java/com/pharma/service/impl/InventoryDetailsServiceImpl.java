package com.pharma.service.impl;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.ExpiredStockView;
import com.pharma.dto.InventoryDetailsDto;
import com.pharma.entity.InventoryDetailsEntity;
import com.pharma.mapper.InventoryDetailsMapper;
import com.pharma.repository.InventoryDetailsRepository;
import com.pharma.service.InventoryDetailsService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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
                .map(inventoryDetailsMapper::mapToDto)
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

}
