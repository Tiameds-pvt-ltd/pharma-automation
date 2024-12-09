package com.project.pharma.service;

import com.project.pharma.dto.SupplierDto;

import java.util.List;

public interface SupplierService {

    SupplierDto createSupplier(SupplierDto supplierDto);

    SupplierDto getSupplierById(Integer supplierId);

    List<SupplierDto> getAllSupplier();

    SupplierDto updateSupplier(Integer supplierId, SupplierDto updatedSupplier);

    void deleteSupplier(Integer supplierId);
}
