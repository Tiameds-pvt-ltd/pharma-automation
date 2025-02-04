package com.pharma.mapper;

import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.StockEntity;
import com.pharma.entity.StockItemEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockMapper {

    // Convert StockEntity to StockDto
    public StockDto toDto(StockEntity stockEntity) {
        if (stockEntity == null) {
            return null;
        }

        StockDto stockDto = new StockDto();
        stockDto.setInvId(stockEntity.getInvId());
        stockDto.setSupplierId(stockEntity.getSupplierId());
        stockDto.setStore(stockEntity.getStore());
        stockDto.setPurchaseBillNo(stockEntity.getPurchaseBillNo());
        stockDto.setPurchaseDate(stockEntity.getPurchaseDate());
        stockDto.setCreditPeriod(stockEntity.getCreditPeriod());
        stockDto.setPaymentDueDate(stockEntity.getPaymentDueDate());
        stockDto.setInvoiceAmount(stockEntity.getInvoiceAmount().intValue());
        stockDto.setTotalAmount(stockEntity.getTotalAmount() != null ? stockEntity.getTotalAmount().doubleValue() : null);
        stockDto.setTotalGst(stockEntity.getTotalGst() != null ? stockEntity.getTotalGst().doubleValue() : null);
        stockDto.setTotalDiscount(stockEntity.getTotalDiscount() != null ? stockEntity.getTotalDiscount().doubleValue() : null);
        stockDto.setGrandTotal(stockEntity.getGrandTotal() != null ? stockEntity.getGrandTotal().doubleValue() : null);
        stockDto.setPaymentStatus(stockEntity.getPaymentStatus());
        stockDto.setGoodStatus(stockEntity.getGoodStatus());

        // Map StockItemEntities to StockItemDtos
        List<StockItemDto> stockItemDtos = stockEntity.getStockItemEntities().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        stockDto.setStockItemDtos(stockItemDtos);

        return stockDto;
    }

    // Convert StockDto to StockEntity
    public StockEntity toEntity(StockDto stockDto) {
        if (stockDto == null) {
            return null;
        }

        StockEntity stockEntity = new StockEntity();
        stockEntity.setSupplierId(stockDto.getSupplierId());
        stockEntity.setStore(stockDto.getStore());
        stockEntity.setPurchaseBillNo(stockDto.getPurchaseBillNo());
        stockEntity.setPurchaseDate(stockDto.getPurchaseDate());
        stockEntity.setCreditPeriod(stockDto.getCreditPeriod());
        stockEntity.setPaymentDueDate(stockDto.getPaymentDueDate());
        stockEntity.setInvoiceAmount(stockDto.getInvoiceAmount() != null ? BigDecimal.valueOf(stockDto.getInvoiceAmount()) : null);
        stockEntity.setTotalAmount(stockDto.getTotalAmount() != null ? BigDecimal.valueOf(stockDto.getTotalAmount()) : null);
        stockEntity.setTotalGst(stockDto.getTotalGst() != null ? BigDecimal.valueOf(stockDto.getTotalGst()) : null);
        stockEntity.setTotalDiscount(stockDto.getTotalDiscount() != null ? BigDecimal.valueOf(stockDto.getTotalDiscount()) : null);
        stockEntity.setGrandTotal(stockDto.getGrandTotal() != null ? BigDecimal.valueOf(stockDto.getGrandTotal()) : null);
        stockEntity.setPaymentStatus(stockDto.getPaymentStatus());
        stockEntity.setGoodStatus(stockDto.getGoodStatus());

        // Map StockItemDtos to StockItemEntities
        List<StockItemEntity> stockItemEntities = stockDto.getStockItemDtos().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        stockItemEntities.forEach(stockItem -> stockItem.setStockEntity(stockEntity)); // Maintain bi-directional mapping
        stockEntity.setStockItemEntities(stockItemEntities);

        return stockEntity;
    }

    // Convert StockItemEntity to StockItemDto
    private StockItemDto toDto(StockItemEntity stockItemEntity) {
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
        stockItemDto.setDiscount(stockItemEntity.getDiscount());
        stockItemDto.setGstPercentage(stockItemEntity.getGstPercentage());
        stockItemDto.setGstAmount(stockItemEntity.getGstAmount());
        stockItemDto.setAmount(stockItemEntity.getAmount());

        return stockItemDto;
    }

    // Convert StockItemDto to StockItemEntity
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
        stockItemEntity.setDiscount(stockItemDto.getDiscount());
        stockItemEntity.setGstPercentage(stockItemDto.getGstPercentage());
        stockItemEntity.setGstAmount(stockItemDto.getGstAmount());
        stockItemEntity.setAmount(stockItemDto.getAmount());

        return stockItemEntity;
    }

    // Add this method to StockMapper
    public void updateEntityFromDto(StockDto stockDto, StockEntity stockEntity) {
        if (stockDto == null || stockEntity == null) {
            return;
        }

        // Update fields of StockEntity from StockDto
        stockEntity.setSupplierId(stockDto.getSupplierId());
        stockEntity.setStore(stockDto.getStore());
        stockEntity.setPurchaseBillNo(stockDto.getPurchaseBillNo());
        stockEntity.setPurchaseDate(stockDto.getPurchaseDate());
        stockEntity.setCreditPeriod(stockDto.getCreditPeriod());
        stockEntity.setPaymentDueDate(stockDto.getPaymentDueDate());
        stockEntity.setInvoiceAmount(stockDto.getInvoiceAmount() != null ? BigDecimal.valueOf(stockDto.getInvoiceAmount()) : null);
        stockEntity.setTotalAmount(stockDto.getTotalAmount() != null ? BigDecimal.valueOf(stockDto.getTotalAmount()) : null);
        stockEntity.setTotalGst(stockDto.getTotalGst() != null ? BigDecimal.valueOf(stockDto.getTotalGst()) : null);
        stockEntity.setTotalDiscount(stockDto.getTotalDiscount() != null ? BigDecimal.valueOf(stockDto.getTotalDiscount()) : null);
        stockEntity.setGrandTotal(stockDto.getGrandTotal() != null ? BigDecimal.valueOf(stockDto.getGrandTotal()) : null);
        stockEntity.setPaymentStatus(stockDto.getPaymentStatus().toUpperCase());
        stockEntity.setGoodStatus(stockDto.getGoodStatus().toUpperCase());

        // Update StockItemEntities if provided in StockDto
        if (stockDto.getStockItemDtos() != null && !stockDto.getStockItemDtos().isEmpty()) {
            List<StockItemEntity> stockItemEntities = stockDto.getStockItemDtos().stream()
                    .map(this::toEntity) // Reuse existing toEntity method for StockItemDto
                    .collect(Collectors.toList());
            stockItemEntities.forEach(item -> item.setStockEntity(stockEntity)); // Maintain bi-directional mapping
            stockEntity.setStockItemEntities(stockItemEntities);
        }
    }

}
