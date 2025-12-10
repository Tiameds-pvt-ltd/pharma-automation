package com.pharma.service;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.InventoryDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.User;

import java.util.List;

public interface InventoryService {

    List<InventoryDto> getAllInventory(Long pharmacyId, User user);

    List<StockItemDto> getExpiredStock(Long pharmacyId, User user);

    List<ExpiredStockDto> getExpiredStockWithSupplier(Long pharmacyId, User user);

//   List<InventoryDto> getAllInventory(Long createdById);

//    List<StockItemDto> getExpiredStock(Long createdById);

//    List<ExpiredStockDto> getExpiredStockWithSupplier(Long createdById);
}
