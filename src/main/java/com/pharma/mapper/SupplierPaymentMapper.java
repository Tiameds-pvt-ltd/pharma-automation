package com.pharma.mapper;

import com.pharma.dto.SupplierPaymentDetailsDto;
import com.pharma.dto.SupplierPaymentDto;
import com.pharma.entity.SupplierPaymentDetailsEntity;
import com.pharma.entity.SupplierPaymentEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SupplierPaymentMapper {

    public SupplierPaymentDto toDto(SupplierPaymentEntity supplierPaymentEntity) {
        if (supplierPaymentEntity == null) {
            return null;
        }

        SupplierPaymentDto supplierPaymentDto = new SupplierPaymentDto();
        supplierPaymentDto.setPaymentId(supplierPaymentEntity.getPaymentId());
        supplierPaymentDto.setSupplierId(supplierPaymentEntity.getSupplierId());
        supplierPaymentDto.setPharmacyId(supplierPaymentEntity.getPharmacyId());
        supplierPaymentDto.setPaymentDate(supplierPaymentEntity.getPaymentDate());
        supplierPaymentDto.setPaymentMode(supplierPaymentEntity.getPaymentMode());
        supplierPaymentDto.setReferenceNo(supplierPaymentEntity.getReferenceNo());
        supplierPaymentDto.setAmountPaid(supplierPaymentEntity.getAmountPaid());
        supplierPaymentDto.setRemark(supplierPaymentEntity.getRemark());
        supplierPaymentDto.setCreatedBy(supplierPaymentEntity.getCreatedBy());
        supplierPaymentDto.setCreatedDate(supplierPaymentEntity.getCreatedDate());
        supplierPaymentDto.setModifiedBy(supplierPaymentEntity.getModifiedBy());
        supplierPaymentDto.setModifiedDate(supplierPaymentEntity.getModifiedDate());

        List<SupplierPaymentDetailsDto> supplierPaymentDetailsDtos = supplierPaymentEntity.getSupplierPaymentDetailsEntities().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        supplierPaymentDto.setSupplierPaymentDetailsDtos(supplierPaymentDetailsDtos);

        return supplierPaymentDto;
    }

    public SupplierPaymentEntity toEntity(SupplierPaymentDto supplierPaymentDto) {
        if (supplierPaymentDto == null) {
            return null;
        }

        SupplierPaymentEntity supplierPaymentEntity = new SupplierPaymentEntity();
        supplierPaymentEntity.setPaymentId(supplierPaymentDto.getPaymentId());
        supplierPaymentEntity.setSupplierId(supplierPaymentDto.getSupplierId());
        supplierPaymentEntity.setPharmacyId(supplierPaymentDto.getPharmacyId());
        supplierPaymentEntity.setPaymentDate(supplierPaymentDto.getPaymentDate());
        supplierPaymentEntity.setPaymentMode(supplierPaymentDto.getPaymentMode());
        supplierPaymentEntity.setReferenceNo(supplierPaymentDto.getReferenceNo());
        supplierPaymentEntity.setAmountPaid(supplierPaymentDto.getAmountPaid());
        supplierPaymentEntity.setRemark(supplierPaymentDto.getRemark());
        supplierPaymentEntity.setCreatedBy(supplierPaymentDto.getCreatedBy());
        supplierPaymentEntity.setCreatedDate(supplierPaymentDto.getCreatedDate());
        supplierPaymentEntity.setModifiedBy(supplierPaymentDto.getModifiedBy());
        supplierPaymentEntity.setModifiedDate(supplierPaymentDto.getModifiedDate());

        List<SupplierPaymentDetailsEntity> supplierPaymentDetailsEntities = supplierPaymentDto.getSupplierPaymentDetailsDtos().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        supplierPaymentDetailsEntities.forEach(supplierPaymentDetails -> supplierPaymentDetails.setSupplierPaymentEntity(supplierPaymentEntity));
        supplierPaymentEntity.setSupplierPaymentDetailsEntities(supplierPaymentDetailsEntities);

        return supplierPaymentEntity;
    }

    private SupplierPaymentDetailsDto toDto(SupplierPaymentDetailsEntity supplierPaymentDetailsEntity) {
        if (supplierPaymentDetailsEntity == null) {
            return null;
        }

        SupplierPaymentDetailsDto supplierPaymentDetailsDto = new SupplierPaymentDetailsDto();
        supplierPaymentDetailsDto.setPaymentDetailsId(supplierPaymentDetailsEntity.getPaymentDetailsId());
        supplierPaymentDetailsDto.setPurchaseBillNo(supplierPaymentDetailsEntity.getPurchaseBillNo());
        supplierPaymentDetailsDto.setClearedAmount(supplierPaymentDetailsEntity.getClearedAmount());
        supplierPaymentDetailsDto.setInvId(supplierPaymentDetailsEntity.getInvId());
        supplierPaymentDetailsDto.setCreatedBy(supplierPaymentDetailsEntity.getCreatedBy());
        supplierPaymentDetailsDto.setCreatedDate(supplierPaymentDetailsEntity.getCreatedDate());
        supplierPaymentDetailsDto.setModifiedBy(supplierPaymentDetailsEntity.getModifiedBy());
        supplierPaymentDetailsDto.setModifiedDate(supplierPaymentDetailsEntity.getModifiedDate());

        return supplierPaymentDetailsDto;
    }


    public SupplierPaymentDetailsEntity toEntity(SupplierPaymentDetailsDto supplierPaymentDetailsDto) {
        if (supplierPaymentDetailsDto == null) {
            return null;
        }

        SupplierPaymentDetailsEntity supplierPaymentDetailsEntity = new SupplierPaymentDetailsEntity();
        supplierPaymentDetailsEntity.setPaymentDetailsId(supplierPaymentDetailsDto.getPaymentDetailsId());
        supplierPaymentDetailsEntity.setPurchaseBillNo(supplierPaymentDetailsDto.getPurchaseBillNo());
        supplierPaymentDetailsEntity.setClearedAmount(supplierPaymentDetailsDto.getClearedAmount());
        supplierPaymentDetailsEntity.setInvId(supplierPaymentDetailsDto.getInvId());
        supplierPaymentDetailsEntity.setCreatedBy(supplierPaymentDetailsDto.getCreatedBy());
        supplierPaymentDetailsEntity.setCreatedDate(supplierPaymentDetailsDto.getCreatedDate());
        supplierPaymentDetailsEntity.setModifiedBy(supplierPaymentDetailsDto.getModifiedBy());
        supplierPaymentDetailsEntity.setModifiedDate(supplierPaymentDetailsDto.getModifiedDate());
        return supplierPaymentDetailsEntity;
    }
}
