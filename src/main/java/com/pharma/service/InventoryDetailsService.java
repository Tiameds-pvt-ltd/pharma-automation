package com.pharma.service;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.InventoryDetailsDto;
import com.pharma.dto.InventoryDto;

import java.util.List;

public interface InventoryDetailsService {

    List<InventoryDetailsDto> getAllInventoryDetails(Long createdById);

    List<ExpiredStockDto> getCurrentYearStockWithSupplier(Long createdById);

}
