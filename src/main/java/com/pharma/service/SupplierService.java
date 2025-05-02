package com.pharma.service;


import com.pharma.dto.SupplierDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface SupplierService {

    SupplierDto createSupplier(SupplierDto supplierDto, User user);

    List<SupplierDto> getAllSupplier(Long createdById);

    SupplierDto getSupplierById(Long createdById, UUID supplierId);

    SupplierDto updateSupplier(Long modifiedById, UUID supplierId, SupplierDto updatedSupplier);

    void deleteSupplier(Long createdById, UUID supplierId);
}
