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
                supplierEntity.getSupplierMobile(),
                supplierEntity.getSupplierEmail(),
                supplierEntity.getSupplierGstinNo(),
                supplierEntity.getSupplierGstType(),
                supplierEntity.getSupplierAddress(),
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
                supplierDto.getSupplierMobile(),
                supplierDto.getSupplierEmail(),
                supplierDto.getSupplierGstinNo(),
                supplierDto.getSupplierGstType(),
                supplierDto.getSupplierAddress(),
                supplierDto.getSupplierStatus(),
                supplierDto.getPharmacyId(),
                supplierDto.getCreatedBy(),
                supplierDto.getCreatedDate(),
                supplierDto.getModifiedBy(),
                supplierDto.getModifiedDate()
        );
    }

}
