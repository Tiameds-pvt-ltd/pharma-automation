package com.pharma.mapper;

import com.pharma.dto.InventoryDetailsDto;

import com.pharma.dto.StockEditDto;
import com.pharma.entity.InventoryDetailsEntity;
import com.pharma.entity.StockEditEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component

public class InventoryDetailsMapper {

    public InventoryDetailsDto toDto(InventoryDetailsEntity inventoryDetailsEntity) {
        if (inventoryDetailsEntity == null) {
            return null;
        }

        InventoryDetailsDto inventoryDetailsDto = new InventoryDetailsDto();
        inventoryDetailsDto.setInvDetailsId(inventoryDetailsEntity.getInvDetailsId());
        inventoryDetailsDto.setItemId(inventoryDetailsEntity.getItemId());
        inventoryDetailsDto.setBatchNo(inventoryDetailsEntity.getBatchNo());
        inventoryDetailsDto.setPackageQuantity(inventoryDetailsEntity.getPackageQuantity());
        inventoryDetailsDto.setExpiryDate(inventoryDetailsEntity.getExpiryDate());
        inventoryDetailsDto.setPurchasePrice(inventoryDetailsEntity.getPurchasePrice());
        inventoryDetailsDto.setMrpSalePrice(inventoryDetailsEntity.getMrpSalePrice());
        inventoryDetailsDto.setPurchasePricePerUnit(inventoryDetailsEntity.getPurchasePricePerUnit());
        inventoryDetailsDto.setMrpSalePricePerUnit(inventoryDetailsEntity.getMrpSalePricePerUnit());
        inventoryDetailsDto.setGstPercentage(inventoryDetailsEntity.getGstPercentage());
        inventoryDetailsDto.setGstAmount(inventoryDetailsEntity.getGstAmount());
        inventoryDetailsDto.setPharmacyId(inventoryDetailsEntity.getPharmacyId());

        inventoryDetailsDto.setCreatedBy(inventoryDetailsEntity.getCreatedBy());
        inventoryDetailsDto.setCreatedDate(inventoryDetailsEntity.getCreatedDate());
        inventoryDetailsDto.setModifiedBy(inventoryDetailsEntity.getModifiedBy());
        inventoryDetailsDto.setModifiedDate(inventoryDetailsEntity.getModifiedDate());

        List<StockEditDto> stockEditDtos = inventoryDetailsEntity.getStockEditEntities().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        inventoryDetailsDto.setStockEditDtos(stockEditDtos);

        return inventoryDetailsDto;

    }


    public InventoryDetailsEntity toEntity(InventoryDetailsDto inventoryDetailsDto) {
        if (inventoryDetailsDto == null) {
            return null;
        }

        InventoryDetailsEntity inventoryDetailsEntity = new InventoryDetailsEntity();
        inventoryDetailsEntity.setInvDetailsId(inventoryDetailsDto.getInvDetailsId());
        inventoryDetailsEntity.setItemId(inventoryDetailsDto.getItemId());
        inventoryDetailsEntity.setBatchNo(inventoryDetailsDto.getBatchNo());
        inventoryDetailsEntity.setPackageQuantity(inventoryDetailsDto.getPackageQuantity());
        inventoryDetailsEntity.setExpiryDate(inventoryDetailsDto.getExpiryDate());
        inventoryDetailsEntity.setPurchasePrice(inventoryDetailsDto.getPurchasePrice());
        inventoryDetailsEntity.setMrpSalePrice(inventoryDetailsDto.getMrpSalePrice());
        inventoryDetailsEntity.setPurchasePricePerUnit(inventoryDetailsDto.getPurchasePricePerUnit());
        inventoryDetailsEntity.setMrpSalePricePerUnit(inventoryDetailsDto.getMrpSalePricePerUnit());
        inventoryDetailsEntity.setGstPercentage(inventoryDetailsDto.getGstPercentage());
        inventoryDetailsEntity.setGstAmount(inventoryDetailsDto.getGstAmount());
        inventoryDetailsEntity.setPharmacyId(inventoryDetailsDto.getPharmacyId());

        inventoryDetailsEntity.setCreatedBy(inventoryDetailsDto.getCreatedBy());
        inventoryDetailsEntity.setCreatedDate(inventoryDetailsDto.getCreatedDate());
        inventoryDetailsEntity.setModifiedBy(inventoryDetailsDto.getModifiedBy());
        inventoryDetailsEntity.setModifiedDate(inventoryDetailsDto.getModifiedDate());


        List<StockEditEntity> stockEditEntities = inventoryDetailsDto.getStockEditDtos().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        stockEditEntities.forEach(stockEdit -> stockEdit.setInventoryDetailsEntity(inventoryDetailsEntity));
        inventoryDetailsEntity.setStockEditEntities(stockEditEntities);

        return inventoryDetailsEntity;
    }


    private StockEditDto toDto(StockEditEntity stockEditEntity) {
        if (stockEditEntity == null) {
            return null;
        }

        StockEditDto stockEditDto = new StockEditDto();
        stockEditDto.setStockEditId(stockEditEntity.getStockEditId());
        stockEditDto.setStockEditedDate(stockEditEntity.getStockEditedDate());
        stockEditDto.setAdjustmentType(stockEditEntity.getAdjustmentType());
        stockEditDto.setUpdatedStockQuantity(stockEditEntity.getUpdatedStockQuantity());
        stockEditDto.setCreatedBy(stockEditEntity.getCreatedBy());
        stockEditDto.setCreatedDate(stockEditEntity.getCreatedDate());
        stockEditDto.setModifiedBy(stockEditEntity.getModifiedBy());
        stockEditDto.setModifiedDate(stockEditEntity.getModifiedDate());


        return stockEditDto;
    }


    public StockEditEntity toEntity(StockEditDto stockEditDto) {
        if (stockEditDto == null) {
            return null;
        }

        StockEditEntity stockEditEntity = new StockEditEntity();
        stockEditEntity.setStockEditId(stockEditDto.getStockEditId());
        stockEditEntity.setStockEditedDate(stockEditDto.getStockEditedDate());
        stockEditEntity.setAdjustmentType(stockEditDto.getAdjustmentType());
        stockEditEntity.setUpdatedStockQuantity(stockEditDto.getUpdatedStockQuantity());
        stockEditEntity.setCreatedBy(stockEditDto.getCreatedBy());
        stockEditEntity.setCreatedDate(stockEditDto.getCreatedDate());
        stockEditEntity.setModifiedBy(stockEditDto.getModifiedBy());
        stockEditEntity.setModifiedDate(stockEditDto.getModifiedDate());

        return stockEditEntity;
    }


}