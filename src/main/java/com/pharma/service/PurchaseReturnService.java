package com.pharma.service;

import com.pharma.dto.PurchaseReturnDto;
import com.pharma.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PurchaseReturnService {

    PurchaseReturnDto savePurchaseReturn(PurchaseReturnDto purchaseReturnDto, User user);

    List<PurchaseReturnDto> getAllPurchaseReturn(Long pharmacyId, User user);

    PurchaseReturnDto getPurchaseReturnById(Long pharmacyId, UUID returnId, User user);

    void deletePurchaseReturnById(Long pharmacyId, UUID returnId, User user);

    BigDecimal getSumReturnAmountBySupplier(UUID supplierId, Long pharmacyId, User user);

//    BigDecimal getSumReturnAmountBySupplierAndCreatedBy(UUID supplierId, Long createdBy);

}
