package com.pharma.mapper;


import com.pharma.dto.StockDto;
import com.pharma.entity.StockEntity;
import com.pharma.entity.StockItemEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import com.pharma.dto.StockItemDto;

@Component
public class StockMapper {
//    public StockDto toDTO(StockEntity stockEntity) {
//        StockDto stockDto = new StockDto();
//        stockDto.setInvId(stockEntity.getInvId());
//        stockDto.setSupplierId(stockEntity.getSupplierId());
//        stockDto.setStore(stockEntity.getStore());
//        stockDto.setPurchaseBillNo(stockEntity.getPurchaseBillNo());
//        stockDto.setPurchaseDate(stockEntity.getPurchaseDate());
//        stockDto.setCreditPeriod(stockEntity.getCreditPeriod());
//        stockDto.setPaymentDueDate(stockEntity.getPaymentDueDate());
//        stockDto.setInvoiceAmount(stockEntity.getInvoiceAmount());
//        stockDto.setTotalAmount(stockEntity.getTotalAmount());
//        stockDto.setTotalGst(stockEntity.getTotalGst());
//        stockDto.setTotalDiscount(stockEntity.getTotalDiscount());
//        stockDto.setGrandTotal(stockEntity.getGrandTotal());
//        stockDto.setPaymentStatus(stockEntity.getPaymentStatus());
//        stockDto.setGoodStatus(stockDto.getGoodStatus());
//        stockDto.setStockItemDtos(stockEntity.getStockItemEntities().stream()
//                .map(this::toDTO).collect(Collectors.toList()));
//        return stockDto;
//    }
//
//    public StockEntity toEntity(StockDto StockDto) {
//        StockEntity stockEntity = new StockEntity();
//        stockEntity.setSupplierId(StockDto.getSupplierId());
//        stockEntity.setStore(StockDto.getStore());
//        stockEntity.setPurchaseBillNo(StockDto.getPurchaseBillNo());
//        stockEntity.setPurchaseDate(StockDto.getPurchaseDate());
//        stockEntity.setCreditPeriod(StockDto.getCreditPeriod());
//        stockEntity.setPaymentDueDate(StockDto.getPaymentDueDate());
//        stockEntity.setInvoiceAmount(StockDto.getInvoiceAmount());
//        stockEntity.setTotalAmount(StockDto.getTotalAmount());
//        stockEntity.setTotalGst(StockDto.getTotalGst());
//        stockEntity.setTotalDiscount(StockDto.getTotalDiscount());
//        stockEntity.setGrandTotal(StockDto.getGrandTotal());
//        stockEntity.setPaymentStatus(StockDto.getPaymentStatus());
//        stockEntity.setGoodStatus(StockDto.getGoodStatus());
//        stockEntity.setStockItemEntities(StockDto.getStockItemDtos().stream()
//                .map(this::toEntity).collect(Collectors.toList()));
//        return stockEntity;
//    }

    public StockItemDto toDTO(StockItemEntity stockItemEntity) {
        StockItemDto stockItemDto = new StockItemDto();
        stockItemDto.setStockId(stockItemEntity.getStockId());
        stockItemDto.setItemId(stockItemEntity.getItemId());
        stockItemDto.setBatchNo(stockItemEntity.getBatchNo());
        stockItemDto.setPackageQuantity(stockItemEntity.getPackageQuantity());
        stockItemDto.setExpiryDate(stockItemEntity.getExpiryDate());
        stockItemDto.setFreeItem(stockItemEntity.getFreeItem());
        stockItemDto.setDiscount(stockItemEntity.getDiscount());
        stockItemDto.setGstPercentage(stockItemEntity.getGstPercentage());
        stockItemDto.setGstAmount(stockItemEntity.getGstAmount());
        stockItemDto.setAmount(stockItemEntity.getAmount());
        return stockItemDto;
    }

    public StockItemEntity toEntity(StockItemDto StockItemDto) {
        StockItemEntity stockItemEntity = new StockItemEntity();
        stockItemEntity.setItemId(StockItemDto.getItemId());
        stockItemEntity.setBatchNo(StockItemDto.getBatchNo());
        stockItemEntity.setPackageQuantity(StockItemDto.getPackageQuantity());
        stockItemEntity.setExpiryDate(StockItemDto.getExpiryDate());
        stockItemEntity.setFreeItem(StockItemDto.getFreeItem());
        stockItemEntity.setDiscount(StockItemDto.getDiscount());
        stockItemEntity.setGstPercentage(StockItemDto.getGstPercentage());
        stockItemEntity.setGstAmount(StockItemDto.getGstAmount());
        stockItemEntity.setAmount(StockItemDto.getAmount());
        return stockItemEntity;
    }
}
