package com.pharma.service;


import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.User;

import java.util.List;

public interface StockService {

    StockDto createStockAndAssociateWithUser(StockDto stockDto, User user);

    List<StockDto> getAllStocks(String userId);

    StockDto getStockById(String userId, Long invId);

//    StockDto updateStock(Long invId, StockDto stockDto, Long userId);

    StockDto updateStock(Long invId, StockDto updatedStock);

    void deleteStock(Long invId, String userId);

    public boolean isBillNoExists(Long supplierId, int year, String purchaseBillNo) ;

    List<StockItemDto> getStockByItemId(String itemId);
}
