package com.pharma.service;


import com.pharma.dto.SupplierDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface SupplierService {

    SupplierDto createSupplier(SupplierDto supplierDto, User user);

    List<SupplierDto> getAllSupplier(Long pharmacyId, User user);

    SupplierDto getSupplierById(Long pharmacyId, UUID supplierId, User user);

    SupplierDto updateSupplier(Long pharmacyId, UUID supplierId, SupplierDto updatedSupplier, User user);

    void deleteSupplier(Long pharmacyId, UUID supplierId, User user);

}
