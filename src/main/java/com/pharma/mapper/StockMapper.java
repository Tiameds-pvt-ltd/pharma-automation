package com.pharma.mapper;

import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.StockEntity;
import com.pharma.entity.StockItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockMapper {

    public StockDto toDto(StockEntity stockEntity) {
        if (stockEntity == null) {
            return null;
        }

        StockDto stockDto = new StockDto();
        stockDto.setInvId(stockEntity.getInvId());
        stockDto.setSupplierId(stockEntity.getSupplierId());
        stockDto.setPharmacyId(stockEntity.getPharmacyId());
        stockDto.setPurchaseBillNo(stockEntity.getPurchaseBillNo());
        stockDto.setPurchaseDate(stockEntity.getPurchaseDate());
        stockDto.setCreditPeriod(stockEntity.getCreditPeriod());
        stockDto.setPaymentDueDate(stockEntity.getPaymentDueDate());
        stockDto.setInvoiceAmount(stockEntity.getInvoiceAmount());
        stockDto.setTotalAmount(stockEntity.getTotalAmount());
        stockDto.setTotalCgst(stockEntity.getTotalCgst());
        stockDto.setTotalSgst(stockEntity.getTotalSgst());
        stockDto.setTotalDiscountPercentage(stockEntity.getTotalDiscountPercentage());
        stockDto.setTotalDiscountAmount(stockEntity.getTotalDiscountAmount());
        stockDto.setGrandTotal(stockEntity.getGrandTotal());
        stockDto.setPaymentStatus(stockEntity.getPaymentStatus());
        stockDto.setGoodStatus(stockEntity.getGoodStatus());
        stockDto.setGrnNo(stockEntity.getGrnNo());
        stockDto.setCreatedBy(stockEntity.getCreatedBy());
        stockDto.setCreatedDate(stockEntity.getCreatedDate());
        stockDto.setModifiedBy(stockEntity.getModifiedBy());
        stockDto.setModifiedDate(stockEntity.getModifiedDate());

        List<StockItemDto> stockItemDtos = stockEntity.getStockItemEntities().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        stockDto.setStockItemDtos(stockItemDtos);

        return stockDto;
    }

    public StockEntity toEntity(StockDto stockDto) {
        if (stockDto == null) {
            return null;
        }

        StockEntity stockEntity = new StockEntity();
        stockEntity.setSupplierId(stockDto.getSupplierId());
        stockEntity.setPharmacyId(stockDto.getPharmacyId());
        stockEntity.setPurchaseBillNo(stockDto.getPurchaseBillNo());
        stockEntity.setPurchaseDate(stockDto.getPurchaseDate());
        stockEntity.setCreditPeriod(stockDto.getCreditPeriod());
        stockEntity.setPaymentDueDate(stockDto.getPaymentDueDate());
        stockEntity.setInvoiceAmount(stockDto.getInvoiceAmount());
        stockEntity.setTotalAmount(stockDto.getTotalAmount());
        stockEntity.setTotalCgst(stockDto.getTotalCgst());
        stockEntity.setTotalSgst(stockDto.getTotalSgst());
        stockEntity.setTotalDiscountPercentage(stockDto.getTotalDiscountPercentage());
        stockEntity.setTotalDiscountAmount(stockDto.getTotalDiscountAmount());
        stockEntity.setGrandTotal(stockDto.getGrandTotal());
        stockEntity.setPaymentStatus(stockDto.getPaymentStatus());
        stockEntity.setGoodStatus(stockDto.getGoodStatus());
        stockEntity.setGrnNo(stockDto.getGrnNo());
        stockEntity.setCreatedBy(stockDto.getCreatedBy());
        stockEntity.setCreatedDate(stockDto.getCreatedDate());
        stockEntity.setModifiedBy(stockDto.getModifiedBy());
        stockEntity.setModifiedDate(stockDto.getModifiedDate());

        List<StockItemEntity> stockItemEntities = stockDto.getStockItemDtos().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        stockItemEntities.forEach(stockItem -> stockItem.setStockEntity(stockEntity));
        stockEntity.setStockItemEntities(stockItemEntities);

        return stockEntity;
    }

    public StockItemDto toDto(StockItemEntity stockItemEntity) {
        if (stockItemEntity == null) {
            return null;
        }

        StockItemDto stockItemDto = new StockItemDto();
        stockItemDto.setStockId(stockItemEntity.getStockId());
        stockItemDto.setItemId(stockItemEntity.getItemId());
        stockItemDto.setBatchNo(stockItemEntity.getBatchNo());
        stockItemDto.setPackageQuantity(stockItemEntity.getPackageQuantity());
        stockItemDto.setExpiryDate(stockItemEntity.getExpiryDate());
        stockItemDto.setFreeItem(stockItemEntity.getFreeItem());
        stockItemDto.setDiscountPercentage(stockItemEntity.getDiscountPercentage());
        stockItemDto.setDiscountAmount(stockItemEntity.getDiscountAmount());
        stockItemDto.setPurchasePrice(stockItemEntity.getPurchasePrice());
        stockItemDto.setMrpSalePrice(stockItemEntity.getMrpSalePrice());
        stockItemDto.setPurchasePricePerUnit(stockItemEntity.getPurchasePricePerUnit());
        stockItemDto.setMrpSalePricePerUnit(stockItemEntity.getMrpSalePricePerUnit());
        stockItemDto.setGstPercentage(stockItemEntity.getGstPercentage());
        stockItemDto.setGstAmount(stockItemEntity.getGstAmount());
        stockItemDto.setAmount(stockItemEntity.getAmount());
        stockItemDto.setPharmacyId(stockItemEntity.getPharmacyId());
        stockItemDto.setCreatedBy(stockItemEntity.getCreatedBy());
        stockItemDto.setCreatedDate(stockItemEntity.getCreatedDate());
        stockItemDto.setModifiedBy(stockItemEntity.getModifiedBy());
        stockItemDto.setModifiedDate(stockItemEntity.getModifiedDate());

        return stockItemDto;
    }

    public StockItemEntity toEntity(StockItemDto stockItemDto) {
        if (stockItemDto == null) {
            return null;
        }

        StockItemEntity stockItemEntity = new StockItemEntity();
        stockItemEntity.setItemId(stockItemDto.getItemId());
        stockItemEntity.setBatchNo(stockItemDto.getBatchNo());
        stockItemEntity.setPackageQuantity(stockItemDto.getPackageQuantity());
        stockItemEntity.setExpiryDate(stockItemDto.getExpiryDate());
        stockItemEntity.setFreeItem(stockItemDto.getFreeItem());
        stockItemEntity.setDiscountPercentage(stockItemDto.getDiscountPercentage());
        stockItemEntity.setDiscountAmount(stockItemDto.getDiscountAmount());
        stockItemEntity.setPurchasePrice(stockItemDto.getPurchasePrice());
        stockItemEntity.setMrpSalePrice(stockItemDto.getMrpSalePrice());
        stockItemEntity.setPurchasePricePerUnit(stockItemDto.getPurchasePricePerUnit());
        stockItemEntity.setMrpSalePricePerUnit(stockItemDto.getMrpSalePricePerUnit());
        stockItemEntity.setGstPercentage(stockItemDto.getGstPercentage());
        stockItemEntity.setGstAmount(stockItemDto.getGstAmount());
        stockItemEntity.setAmount(stockItemDto.getAmount());
        stockItemEntity.setPharmacyId(stockItemDto.getPharmacyId());
        stockItemEntity.setCreatedBy(stockItemDto.getCreatedBy());
        stockItemEntity.setCreatedDate(stockItemDto.getCreatedDate());
        stockItemEntity.setModifiedBy(stockItemDto.getModifiedBy());
        stockItemEntity.setModifiedDate(stockItemDto.getModifiedDate());
        return stockItemEntity;
    }

}
