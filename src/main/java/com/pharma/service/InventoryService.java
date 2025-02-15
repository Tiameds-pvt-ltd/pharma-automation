package com.pharma.service;

import com.pharma.dto.InventoryDto;
import com.pharma.dto.StockItemDto;

import java.util.List;

public interface InventoryService {

    List<InventoryDto> getAllInventory();

    List<StockItemDto> getExpiredStock();
}
