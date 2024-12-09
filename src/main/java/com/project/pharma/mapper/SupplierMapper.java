package com.project.pharma.mapper;

import com.project.pharma.dto.SupplierDto;
import com.project.pharma.entity.SupplierEntity;

public class SupplierMapper {
    public static SupplierDto mapToDto(SupplierEntity supplierEntity){
        return new SupplierDto(
                supplierEntity.getSupplierId(),
                supplierEntity.getSupplierName(),
                supplierEntity.getSupplierMobile(),
                supplierEntity.getSupplierEmail(),
                supplierEntity.getSupplierGstinNo(),
                supplierEntity.getSupplierGstType(),
                supplierEntity.getSupplierAddress()

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
                supplierDto.getSupplierAddress()
        );
    }

}
