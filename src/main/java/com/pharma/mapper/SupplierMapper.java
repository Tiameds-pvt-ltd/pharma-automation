package com.pharma.mapper;

import com.pharma.dto.SupplierDto;
import com.pharma.entity.SupplierEntity;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {
    public SupplierDto mapToDto(SupplierEntity supplierEntity){
        return new SupplierDto(
                supplierEntity.getSupplierId(),
                supplierEntity.getSupplierName(),
                supplierEntity.getContactPerson(),
                supplierEntity.getSupplierMobile(),
                supplierEntity.getSupplierEmail(),
                supplierEntity.getSupplierGstinNo(),
                supplierEntity.getSupplierGstType(),
                supplierEntity.getSupplierDlno(),
                supplierEntity.getSupplierAddress(),
                supplierEntity.getSupplierStreet(),
                supplierEntity.getSupplierCity(),
                supplierEntity.getSupplierZip(),
                supplierEntity.getSupplierState(),
                supplierEntity.getSupplierStatus(),
                supplierEntity.getPharmacyId(),
                supplierEntity.getCreatedBy(),
                supplierEntity.getCreatedDate(),
                supplierEntity.getModifiedBy(),
                supplierEntity.getModifiedDate()
        );
    }

    public static SupplierEntity mapToEntity(SupplierDto supplierDto){
        return new SupplierEntity(
                supplierDto.getSupplierId(),
                supplierDto.getSupplierName(),
                supplierDto.getContactPerson(),
                supplierDto.getSupplierMobile(),
                supplierDto.getSupplierEmail(),
                supplierDto.getSupplierGstinNo(),
                supplierDto.getSupplierGstType(),
                supplierDto.getSupplierDlno(),
                supplierDto.getSupplierAddress(),
                supplierDto.getSupplierStreet(),
                supplierDto.getSupplierCity(),
                supplierDto.getSupplierZip(),
                supplierDto.getSupplierState(),
                supplierDto.getSupplierStatus(),
                supplierDto.getPharmacyId(),
                supplierDto.getCreatedBy(),
                supplierDto.getCreatedDate(),
                supplierDto.getModifiedBy(),
                supplierDto.getModifiedDate()
        );
    }

}
