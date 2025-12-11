package com.pharma.service;


import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.dto.StockSummaryDto;
import com.pharma.entity.StockItemEntity;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface StockService {

    StockDto saveStock(StockDto stockDto, User user);

    List<StockDto> getAllStocks(Long pharmacyId, User user);

    StockDto getStockById(Long pharmacyId, UUID invId, User user);

    void deleteStock(Long pharmacyId, UUID invId, User user);

//    void confirmPayment(Long createdById, UUID invId);

//    List<StockItemDto> getStockByItemId(Long createdById,UUID itemId)

//    boolean isBillNoExists(UUID supplierId, int year, String purchaseBillNo);

//    List<StockSummaryDto> getStocksByPaymentStatusAndSupplierAndCreatedBy(String paymentStatus, UUID supplierId, Long createdBy);

//    StockItemDto updateStockItem(Long modifiedById, UUID invId, UUID itemId, String batchNo, StockItemDto updatedItem);

    List<StockItemDto> getStockByItemId(Long pharmacyId, UUID itemId, User user);

    List<StockItemDto> getItemsBySupplierId(Long pharmacyId, UUID supplierId, User user);

    boolean isBillNoExists(UUID supplierId, Long pharmacyId, int year, String purchaseBillNo, User user);

    List<StockSummaryDto> getStocksByPaymentStatusAndSupplierAndPharmacy(String paymentStatus, UUID supplierId, Long pharmacyId, User user);

    StockItemDto updateStockItem(
            User user,
            Long modifiedById,
            Long pharmacyId,
            UUID invId,
            UUID itemId,
            String batchNo,
            StockItemDto updatedItem
    );

    void confirmPayment(Long pharmacyId, UUID invId, User user);


}
