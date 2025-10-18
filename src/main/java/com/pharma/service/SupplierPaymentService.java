package com.pharma.service;

import com.pharma.dto.SupplierPaymentDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface SupplierPaymentService {

    SupplierPaymentDto saveSupplierPayment(SupplierPaymentDto supplierPaymentDto, User user);

    List<SupplierPaymentDto> getAllSupplierPayment(Long createdById);

    SupplierPaymentDto getSupplierPaymentById(Long createdById, UUID paymentId);
}
