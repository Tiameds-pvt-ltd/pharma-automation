package com.pharma.mapper;

import com.pharma.dto.InventoryDetailsDto;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.dto.PurchaseOrderItemDto;
import com.pharma.entity.InventoryDetailsEntity;
import com.pharma.entity.PurchaseOrderEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InventoryDetailsMapper {

//    public InventoryDetailsDto toDto(InventoryDetailsEntity inventoryDetailsEntity){
//        if (inventoryDetailsEntity == null) {
//            return null;
//        }
//
//        InventoryDetailsDto inventoryDetailsDto = new InventoryDetailsDto();
//        inventoryDetailsDto.setInvDetailsId(inventoryDetailsEntity.getInvDetailsId());
//        inventoryDetailsDto.setItemId(inventoryDetailsEntity.getItemId());
//        inventoryDetailsDto.setBatchNo(inventoryDetailsEntity.getBatchNo());
//        inventoryDetailsDto.setPackageQuantity(inventoryDetailsEntity.getPackageQuantity());
//        inventoryDetailsDto.setExpiryDate(inventoryDetailsEntity.getExpiryDate());
//        inventoryDetailsDto.setPurchasePrice(inventoryDetailsEntity.getPurchasePrice());
//        inventoryDetailsDto.setMrpSalePrice(inventoryDetailsEntity.getMrpSalePrice());
//        inventoryDetailsDto.setPurchasePricePerUnit(inventoryDetailsEntity.getPurchasePricePerUnit());
//        inventoryDetailsDto.setGstPercentage(inventoryDetailsEntity.getGstPercentage());
//        inventoryDetailsDto.setGstAmount(inventoryDetailsEntity.getGstAmount());
//
//        purchaseOrderDto.setCreatedBy(purchaseOrderEntity.getCreatedBy());
//        purchaseOrderDto.setCreatedDate(purchaseOrderEntity.getCreatedDate());
//        purchaseOrderDto.setModifiedBy(purchaseOrderEntity.getModifiedBy());
//        purchaseOrderDto.setModifiedDate(purchaseOrderEntity.getModifiedDate());
//
//        List<PurchaseOrderItemDto> purchaseOrderItemDtos = purchaseOrderEntity.getPurchaseOrderItemEntities().stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//        purchaseOrderDto.setPurchaseOrderItemDtos(purchaseOrderItemDtos);
//
//        return purchaseOrderDto;
//
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//





//    public InventoryDetailsDto mapToDto(InventoryDetailsEntity inventoryDetailsEntity){
//        return new InventoryDetailsDto(
//                inventoryDetailsEntity.getInvDetailsId(),
//                inventoryDetailsEntity.getItemId(),
//                inventoryDetailsEntity.getBatchNo(),
//                inventoryDetailsEntity.getPackageQuantity(),
//                inventoryDetailsEntity.getExpiryDate(),
//                inventoryDetailsEntity.getPurchasePrice(),
//                inventoryDetailsEntity.getMrpSalePrice(),
//                inventoryDetailsEntity.getPurchasePricePerUnit(),
//                inventoryDetailsEntity.getMrpSalePricePerUnit(),
//                inventoryDetailsEntity.getGstPercentage(),
//                inventoryDetailsEntity.getGstAmount(),
//                inventoryDetailsEntity.getCreatedBy(),
//                inventoryDetailsEntity.getCreatedDate(),
//                inventoryDetailsEntity.getModifiedBy(),
//                inventoryDetailsEntity.getModifiedDate()
//        );
//    }
//
//
//    public static InventoryDetailsEntity mapToEntity(InventoryDetailsDto inventoryDetailsDto){
//        return  new InventoryDetailsEntity(
//                inventoryDetailsDto.getInvDetailsId(),
//                inventoryDetailsDto.getItemId(),
//                inventoryDetailsDto.getBatchNo(),
//                inventoryDetailsDto.getPackageQuantity(),
//                inventoryDetailsDto.getExpiryDate(),
//                inventoryDetailsDto.getPurchasePrice(),
//                inventoryDetailsDto.getMrpSalePrice(),
//                inventoryDetailsDto.getPurchasePricePerUnit(),
//                inventoryDetailsDto.getMrpSalePricePerUnit(),
//                inventoryDetailsDto.getGstPercentage(),
//                inventoryDetailsDto.getGstAmount(),
//                inventoryDetailsDto.getCreatedBy(),
//                inventoryDetailsDto.getCreatedDate(),
//                inventoryDetailsDto.getModifiedBy(),
//                inventoryDetailsDto.getModifiedDate()
//
//        );
//    }
}
