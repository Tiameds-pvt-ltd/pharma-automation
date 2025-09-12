package com.pharma.service;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.ExpiredStockView;
import com.pharma.dto.InventoryDetailsDto;
import com.pharma.dto.InventoryDto;
import com.pharma.entity.User;

import java.util.List;

public interface InventoryDetailsService {

    List<InventoryDetailsDto> getAllInventoryDetails(Long createdById);

    List<ExpiredStockDto> getCurrentYearStockWithSupplier(Long createdById);

    List<ExpiredStockView> getNextThreeMonthsStockWithSupplier(Long createdById);

    InventoryDetailsDto saveInventoryDetails(InventoryDetailsDto inventoryDetailsDto, User user);
}
