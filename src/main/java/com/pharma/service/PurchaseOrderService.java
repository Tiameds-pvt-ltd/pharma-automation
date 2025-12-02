package com.pharma.service;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface PurchaseOrderService {

    PurchaseOrderDto savePurchaseOrder(PurchaseOrderDto purchaseOrderDto, User user);

    List<PurchaseOrderDto> getAllPurchaseOrders(Long pharmacyId, User user);

    PurchaseOrderDto getPurchaseOrderById(Long pharmacyId, UUID orderId, User user);

    void deletePurchaseOrderById(Long pharmacyId, UUID orderId, User user);

}
