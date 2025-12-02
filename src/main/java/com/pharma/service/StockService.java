package com.pharma.service;


import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.dto.StockSummaryDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface StockService {

    StockDto saveStock(StockDto stockDto, User user);

    List<StockDto> getAllStocks(Long pharmacyId, User user);

    StockDto getStockById(Long pharmacyId, UUID invId, User user);

    void deleteStock(Long pharmacyId, UUID invId, User user);

    void confirmPayment(Long createdById, UUID invId);

    List<StockItemDto> getStockByItemId(Long createdById,UUID itemId);

    boolean isBillNoExists(UUID supplierId, int year, String purchaseBillNo);

    List<StockSummaryDto> getStocksByPaymentStatusAndSupplierAndCreatedBy(String paymentStatus, UUID supplierId, Long createdBy);

    StockItemDto updateStockItem(Long modifiedById, UUID invId, UUID itemId, String batchNo, StockItemDto updatedItem);

}
