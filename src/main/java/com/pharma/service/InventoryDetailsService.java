package com.pharma.service;

import com.pharma.dto.*;
import com.pharma.entity.User;

import java.util.List;

public interface InventoryDetailsService {

    List<InventoryDetailsDto> getAllInventoryDetails(Long pharmacyId, User user);

    List<ExpiredStockDto> getCurrentYearStockWithSupplier(Long pharmacyId, User user);

    List<ExpiredStockView> getNextThreeMonthsStockWithSupplier(Long pharmacyId, User user);

    List<InventoryView> getInventory(Long pharmacyId, User user);

//    List<ExpiredStockDto> getCurrentYearStockWithSupplier(Long createdById);

//    List<ExpiredStockView> getNextThreeMonthsStockWithSupplier(Long createdById);

    InventoryDetailsDto saveInventoryDetails(InventoryDetailsDto inventoryDetailsDto, User user);
}
