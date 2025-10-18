package com.pharma.service;

import com.pharma.dto.PurchaseReturnDto;
import com.pharma.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PurchaseReturnService {

    PurchaseReturnDto savePurchaseReturn(PurchaseReturnDto purchaseReturnDto, User user);

    List<PurchaseReturnDto> getAllPurchaseReturn(Long createdById);

    PurchaseReturnDto getPurchaseReturnById(Long createdById, UUID returnId);

    void deletePurchaseReturnById(Long createdById, UUID returnId);

    BigDecimal getSumReturnAmountBySupplierAndCreatedBy(UUID supplierId, Long createdBy);

}
