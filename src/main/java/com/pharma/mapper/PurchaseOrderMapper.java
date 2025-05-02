package com.pharma.mapper;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.dto.PurchaseOrderItemDto;
import com.pharma.entity.PurchaseOrderEntity;
import com.pharma.entity.PurchaseOrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseOrderMapper {

    public PurchaseOrderDto toDto(PurchaseOrderEntity purchaseOrderEntity){
        if (purchaseOrderEntity == null) {
            return null;
        }

        PurchaseOrderDto purchaseOrderDto = new PurchaseOrderDto();
        purchaseOrderDto.setOrderId(purchaseOrderEntity.getOrderId());
        purchaseOrderDto.setOrderId1(purchaseOrderEntity.getOrderId1());
        purchaseOrderDto.setPharmacyId(purchaseOrderEntity.getPharmacyId());
        purchaseOrderDto.setPharmacistId(purchaseOrderEntity.getPharmacistId());
        purchaseOrderDto.setSupplierId(purchaseOrderEntity.getSupplierId());
        purchaseOrderDto.setOrderedDate(purchaseOrderEntity.getOrderedDate());
        purchaseOrderDto.setIntendedDeliveryDate(purchaseOrderEntity.getIntendedDeliveryDate());
        purchaseOrderDto.setTotalAmount(purchaseOrderEntity.getTotalAmount());
        purchaseOrderDto.setTotalGst(purchaseOrderEntity.getTotalGst());
        purchaseOrderDto.setGrandTotal(purchaseOrderEntity.getGrandTotal());
        purchaseOrderDto.setCreatedBy(purchaseOrderEntity.getCreatedBy());
        purchaseOrderDto.setCreatedDate(purchaseOrderEntity.getCreatedDate());
        purchaseOrderDto.setModifiedBy(purchaseOrderEntity.getModifiedBy());
        purchaseOrderDto.setModifiedDate(purchaseOrderEntity.getModifiedDate());

        List<PurchaseOrderItemDto> purchaseOrderItemDtos = purchaseOrderEntity.getPurchaseOrderItemEntities().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        purchaseOrderDto.setPurchaseOrderItemDtos(purchaseOrderItemDtos);

       return purchaseOrderDto;

    }

    public PurchaseOrderEntity toEntity(PurchaseOrderDto purchaseOrderDto) {
        if (purchaseOrderDto == null) {
            return null;
        }

        PurchaseOrderEntity purchaseOrderEntity = new PurchaseOrderEntity();
        purchaseOrderEntity.setOrderId(purchaseOrderDto.getOrderId());
        purchaseOrderEntity.setOrderId1(purchaseOrderDto.getOrderId1());
        purchaseOrderEntity.setPharmacyId(purchaseOrderDto.getPharmacyId());
        purchaseOrderEntity.setPharmacistId(purchaseOrderDto.getPharmacistId());
        purchaseOrderEntity.setSupplierId(purchaseOrderDto.getSupplierId());
        purchaseOrderEntity.setOrderedDate(purchaseOrderDto.getOrderedDate());
        purchaseOrderEntity.setIntendedDeliveryDate(purchaseOrderDto.getIntendedDeliveryDate());
        purchaseOrderEntity.setTotalAmount(purchaseOrderDto.getTotalAmount());
        purchaseOrderEntity.setTotalGst(purchaseOrderDto.getTotalGst());
        purchaseOrderEntity.setGrandTotal(purchaseOrderDto.getGrandTotal());
        purchaseOrderEntity.setCreatedBy(purchaseOrderDto.getCreatedBy());
        purchaseOrderEntity.setCreatedDate(purchaseOrderDto.getCreatedDate());
        purchaseOrderEntity.setModifiedBy(purchaseOrderDto.getModifiedBy());
        purchaseOrderEntity.setModifiedDate(purchaseOrderDto.getModifiedDate());


        List<PurchaseOrderItemEntity> purchaseOrderItemEntities = purchaseOrderDto.getPurchaseOrderItemDtos().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        purchaseOrderItemEntities.forEach(purchaseOrderItem -> purchaseOrderItem.setPurchaseOrderEntity(purchaseOrderEntity)); // Maintain bi-directional mapping
        purchaseOrderEntity.setPurchaseOrderItemEntities(purchaseOrderItemEntities);

        return purchaseOrderEntity;
    }

    private PurchaseOrderItemDto toDto(PurchaseOrderItemEntity purchaseOrderItemEntity) {
        if (purchaseOrderItemEntity == null) {
            return null;
        }

        PurchaseOrderItemDto purchaseOrderItemDto = new PurchaseOrderItemDto();
        purchaseOrderItemDto.setOrderItemId(purchaseOrderItemEntity.getOrderItemId());
        purchaseOrderItemDto.setItemId(purchaseOrderItemEntity.getItemId());
        purchaseOrderItemDto.setQuantity(purchaseOrderItemEntity.getQuantity());
        purchaseOrderItemDto.setManufacturer(purchaseOrderItemEntity.getManufacturer());
        purchaseOrderItemDto.setGstPercentage(purchaseOrderItemEntity.getGstPercentage());
        purchaseOrderItemDto.setGstAmount(purchaseOrderItemEntity.getGstAmount());
        purchaseOrderItemDto.setAmount(purchaseOrderItemEntity.getAmount());
        purchaseOrderItemDto.setUnitTypeId(purchaseOrderItemEntity.getUnitTypeId());
        purchaseOrderItemDto.setVariantTypeId(purchaseOrderItemEntity.getVariantTypeId());
        purchaseOrderItemDto.setCreatedBy(purchaseOrderItemEntity.getCreatedBy());
        purchaseOrderItemDto.setCreatedDate(purchaseOrderItemEntity.getCreatedDate());
        purchaseOrderItemDto.setModifiedBy(purchaseOrderItemEntity.getModifiedBy());
        purchaseOrderItemDto.setModifiedDate(purchaseOrderItemEntity.getModifiedDate());


        return purchaseOrderItemDto;
    }

    public PurchaseOrderItemEntity toEntity(PurchaseOrderItemDto purchaseOrderItemDto) {
        if (purchaseOrderItemDto == null) {
            return null;
        }

        PurchaseOrderItemEntity purchaseOrderItemEntity = new PurchaseOrderItemEntity();
        purchaseOrderItemEntity.setOrderItemId(purchaseOrderItemDto.getOrderItemId());
        purchaseOrderItemEntity.setItemId(purchaseOrderItemDto.getItemId());
        purchaseOrderItemEntity.setQuantity(purchaseOrderItemDto.getQuantity());
        purchaseOrderItemEntity.setManufacturer(purchaseOrderItemDto.getManufacturer());
        purchaseOrderItemEntity.setGstPercentage(purchaseOrderItemDto.getGstPercentage());
        purchaseOrderItemEntity.setGstAmount(purchaseOrderItemDto.getGstAmount());
        purchaseOrderItemEntity.setAmount(purchaseOrderItemDto.getAmount());
        purchaseOrderItemEntity.setUnitTypeId(purchaseOrderItemDto.getUnitTypeId());
        purchaseOrderItemEntity.setVariantTypeId(purchaseOrderItemDto.getVariantTypeId());
        purchaseOrderItemEntity.setCreatedBy(purchaseOrderItemDto.getCreatedBy());
        purchaseOrderItemEntity.setCreatedDate(purchaseOrderItemDto.getCreatedDate());
        purchaseOrderItemEntity.setModifiedBy(purchaseOrderItemDto.getModifiedBy());
        purchaseOrderItemEntity.setModifiedDate(purchaseOrderItemDto.getModifiedDate());

        return purchaseOrderItemEntity;
    }

    }
