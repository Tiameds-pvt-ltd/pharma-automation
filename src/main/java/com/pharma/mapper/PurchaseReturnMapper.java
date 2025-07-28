package com.pharma.mapper;

import com.pharma.dto.*;
import com.pharma.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseReturnMapper {

    public PurchaseReturnDto toDto(PurchaseReturnEntity purchaseReturnEntity){
        if (purchaseReturnEntity == null) {
            return null;
        }

        PurchaseReturnDto purchaseReturnDto = new PurchaseReturnDto();
        purchaseReturnDto.setReturnId(purchaseReturnEntity.getReturnId());
        purchaseReturnDto.setReturnId1(purchaseReturnEntity.getReturnId1());
        purchaseReturnDto.setInvId(purchaseReturnEntity.getInvId());
        purchaseReturnDto.setReturnDate(purchaseReturnEntity.getReturnDate());
        purchaseReturnDto.setSupplierId(purchaseReturnEntity.getSupplierId());
        purchaseReturnDto.setPurchaseBillNo(purchaseReturnEntity.getPurchaseBillNo());
        purchaseReturnDto.setTotalAmount(purchaseReturnEntity.getTotalAmount());
        purchaseReturnDto.setTotalGst(purchaseReturnEntity.getTotalGst());
        purchaseReturnDto.setReturnAmount(purchaseReturnEntity.getReturnAmount());
        purchaseReturnDto.setPharmacyId(purchaseReturnEntity.getPharmacyId());
        purchaseReturnDto.setRemark(purchaseReturnEntity.getRemark());
        purchaseReturnDto.setCreatedBy(purchaseReturnEntity.getCreatedBy());
        purchaseReturnDto.setCreatedDate(purchaseReturnEntity.getCreatedDate());
        purchaseReturnDto.setModifiedBy(purchaseReturnEntity.getModifiedBy());
        purchaseReturnDto.setModifiedDate(purchaseReturnEntity.getModifiedDate());

        List<PurchaseReturnItemDto> purchaseReturnItemDtos = purchaseReturnEntity.getPurchaseReturnItemEntities().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        purchaseReturnDto.setPurchaseReturnItemDtos(purchaseReturnItemDtos);

        return purchaseReturnDto;

    }


    public PurchaseReturnEntity toEntity(PurchaseReturnDto purchaseReturnDto) {
        if (purchaseReturnDto == null) {
            return null;
        }

        PurchaseReturnEntity purchaseReturnEntity = new PurchaseReturnEntity();
        purchaseReturnEntity.setReturnId(purchaseReturnDto.getReturnId());
        purchaseReturnEntity.setReturnId1(purchaseReturnDto.getReturnId1());
        purchaseReturnEntity.setInvId(purchaseReturnDto.getInvId());
        purchaseReturnEntity.setReturnDate(purchaseReturnDto.getReturnDate());
        purchaseReturnEntity.setSupplierId(purchaseReturnDto.getSupplierId());
        purchaseReturnEntity.setPurchaseBillNo(purchaseReturnDto.getPurchaseBillNo());
        purchaseReturnEntity.setTotalAmount(purchaseReturnDto.getTotalAmount());
        purchaseReturnEntity.setTotalGst(purchaseReturnDto.getTotalGst());
        purchaseReturnEntity.setReturnAmount(purchaseReturnDto.getReturnAmount());
        purchaseReturnEntity.setPharmacyId(purchaseReturnDto.getPharmacyId());
        purchaseReturnEntity.setRemark(purchaseReturnDto.getRemark());


        purchaseReturnEntity.setCreatedBy(purchaseReturnDto.getCreatedBy());
        purchaseReturnEntity.setCreatedDate(purchaseReturnDto.getCreatedDate());
        purchaseReturnEntity.setModifiedBy(purchaseReturnDto.getModifiedBy());
        purchaseReturnEntity.setModifiedDate(purchaseReturnDto.getModifiedDate());

        List<PurchaseReturnItemEntity> purchaseReturnItemEntities = purchaseReturnDto.getPurchaseReturnItemDtos().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        purchaseReturnItemEntities.forEach(purchaseReturnItem -> purchaseReturnItem.setPurchaseReturnEntity(purchaseReturnEntity)); // Maintain bi-directional mapping
        purchaseReturnEntity.setPurchaseReturnItemEntities(purchaseReturnItemEntities);

        return purchaseReturnEntity;
    }

    private PurchaseReturnItemDto toDto(PurchaseReturnItemEntity purchaseReturnItemEntity) {
        if (purchaseReturnItemEntity == null) {
            return null;
        }

        PurchaseReturnItemDto purchaseReturnItemDto = new PurchaseReturnItemDto();
        purchaseReturnItemDto.setReturnItemId(purchaseReturnItemEntity.getReturnItemId());
        purchaseReturnItemDto.setItemId(purchaseReturnItemEntity.getItemId());
        purchaseReturnItemDto.setBatchNo(purchaseReturnItemEntity.getBatchNo());

        purchaseReturnItemDto.setReturnType(purchaseReturnItemEntity.getReturnType());
        purchaseReturnItemDto.setReturnQuantity(purchaseReturnItemEntity.getReturnQuantity());
        purchaseReturnItemDto.setGstPercentage(purchaseReturnItemEntity.getGstPercentage());
        purchaseReturnItemDto.setDiscrepancyIn(purchaseReturnItemEntity.getDiscrepancyIn());
        purchaseReturnItemDto.setDiscrepancy(purchaseReturnItemEntity.getDiscrepancy());

        purchaseReturnItemDto.setCreatedBy(purchaseReturnItemEntity.getCreatedBy());
        purchaseReturnItemDto.setCreatedDate(purchaseReturnItemEntity.getCreatedDate());
        purchaseReturnItemDto.setModifiedBy(purchaseReturnItemEntity.getModifiedBy());
        purchaseReturnItemDto.setModifiedDate(purchaseReturnItemEntity.getModifiedDate());

        return purchaseReturnItemDto;
    }

    public PurchaseReturnItemEntity toEntity(PurchaseReturnItemDto purchaseReturnItemDto) {
        if (purchaseReturnItemDto == null) {
            return null;
        }

        PurchaseReturnItemEntity purchaseReturnItemEntity =  new PurchaseReturnItemEntity();
        purchaseReturnItemEntity.setReturnItemId(purchaseReturnItemDto.getReturnItemId());
        purchaseReturnItemEntity.setItemId(purchaseReturnItemDto.getItemId());
        purchaseReturnItemEntity.setBatchNo(purchaseReturnItemDto.getBatchNo());

        purchaseReturnItemEntity.setReturnType(purchaseReturnItemDto.getReturnType());
        purchaseReturnItemEntity.setReturnQuantity(purchaseReturnItemDto.getReturnQuantity());
        purchaseReturnItemEntity.setGstPercentage(purchaseReturnItemDto.getGstPercentage());
        purchaseReturnItemEntity.setDiscrepancyIn(purchaseReturnItemDto.getDiscrepancyIn());
        purchaseReturnItemEntity.setDiscrepancy(purchaseReturnItemDto.getDiscrepancy());

        purchaseReturnItemEntity.setCreatedBy(purchaseReturnItemDto.getCreatedBy());
        purchaseReturnItemEntity.setCreatedDate(purchaseReturnItemDto.getCreatedDate());
        purchaseReturnItemEntity.setModifiedBy(purchaseReturnItemDto.getModifiedBy());
        purchaseReturnItemEntity.setModifiedDate(purchaseReturnItemDto.getModifiedDate());

        return purchaseReturnItemEntity;
    }
}
