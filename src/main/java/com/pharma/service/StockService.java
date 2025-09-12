package com.pharma.service;


import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface StockService {

    StockDto saveStock(StockDto stockDto, User user);

    List<StockDto> getAllStocks(Long createdById);

    StockDto getStockById(Long createdById, UUID invId);

    void deleteStock(Long createdById, UUID invId);

    void confirmPayment(Long createdById, UUID invId);

    List<StockItemDto> getStockByItemId(Long createdById,UUID itemId);

    boolean isBillNoExists(UUID supplierId, int year, String purchaseBillNo);

}
