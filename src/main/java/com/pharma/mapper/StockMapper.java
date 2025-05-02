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
        stockDto.setPurchaseBillNo(stockEntity.getPurchaseBillNo());
        stockDto.setPurchaseDate(stockEntity.getPurchaseDate());
        stockDto.setCreditPeriod(stockEntity.getCreditPeriod());
        stockDto.setPaymentDueDate(stockEntity.getPaymentDueDate());
        stockDto.setInvoiceAmount(stockEntity.getInvoiceAmount());
        stockDto.setTotalAmount(stockEntity.getTotalAmount());
        stockDto.setTotalCgst(stockEntity.getTotalCgst());
        stockDto.setTotalSgst(stockEntity.getTotalSgst());
        stockDto.setTotalDiscount(stockEntity.getTotalDiscount());
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
        stockEntity.setPurchaseBillNo(stockDto.getPurchaseBillNo());
        stockEntity.setPurchaseDate(stockDto.getPurchaseDate());
        stockEntity.setCreditPeriod(stockDto.getCreditPeriod());
        stockEntity.setPaymentDueDate(stockDto.getPaymentDueDate());
        stockEntity.setInvoiceAmount(stockDto.getInvoiceAmount());
        stockEntity.setTotalAmount(stockDto.getTotalAmount());
        stockEntity.setTotalCgst(stockDto.getTotalCgst());
        stockEntity.setTotalSgst(stockDto.getTotalSgst());
        stockEntity.setTotalDiscount(stockDto.getTotalDiscount());
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

    private StockItemDto toDto(StockItemEntity stockItemEntity) {
        if (stockItemEntity == null) {
            return null;
        }

        StockItemDto stockItemDto = new StockItemDto();
        stockItemDto.setStockId(stockItemEntity.getStockId());
        stockItemDto.setItemId(stockItemEntity.getItemId());
        stockItemDto.setPharmacyId(stockItemEntity.getPharmacyId());
        stockItemDto.setBatchNo(stockItemEntity.getBatchNo());
        stockItemDto.setPackageQuantity(stockItemEntity.getPackageQuantity());
        stockItemDto.setExpiryDate(stockItemEntity.getExpiryDate());
        stockItemDto.setFreeItem(stockItemEntity.getFreeItem());
        stockItemDto.setDiscount(stockItemEntity.getDiscount());
        stockItemDto.setPurchasePrice(stockItemEntity.getPurchasePrice());
        stockItemDto.setMrpSalePrice(stockItemEntity.getMrpSalePrice());
        stockItemDto.setPurchasePricePerUnit(stockItemEntity.getPurchasePricePerUnit());
        stockItemDto.setMrpSalePricePerUnit(stockItemEntity.getMrpSalePricePerUnit());
        stockItemDto.setCgstPercentage(stockItemEntity.getCgstPercentage());
        stockItemDto.setSgstPercentage(stockItemEntity.getSgstPercentage());
        stockItemDto.setCgstAmount(stockItemEntity.getCgstAmount());
        stockItemDto.setSgstAmount(stockItemEntity.getSgstAmount());
        stockItemDto.setAmount(stockItemEntity.getAmount());
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
        stockItemEntity.setPharmacyId(stockItemDto.getPharmacyId());
        stockItemEntity.setBatchNo(stockItemDto.getBatchNo());
        stockItemEntity.setPackageQuantity(stockItemDto.getPackageQuantity());
        stockItemEntity.setExpiryDate(stockItemDto.getExpiryDate());
        stockItemEntity.setFreeItem(stockItemDto.getFreeItem());
        stockItemEntity.setDiscount(stockItemDto.getDiscount());
        stockItemEntity.setPurchasePrice(stockItemDto.getPurchasePrice());
        stockItemEntity.setMrpSalePrice(stockItemDto.getMrpSalePrice());
        stockItemEntity.setCgstPercentage(stockItemDto.getCgstPercentage());
        stockItemEntity.setSgstPercentage(stockItemDto.getSgstPercentage());
        stockItemEntity.setPurchasePricePerUnit(stockItemDto.getPurchasePricePerUnit());
        stockItemEntity.setMrpSalePricePerUnit(stockItemDto.getMrpSalePricePerUnit());
        stockItemEntity.setCgstAmount(stockItemDto.getCgstAmount());
        stockItemEntity.setSgstAmount(stockItemDto.getSgstAmount());
        stockItemEntity.setAmount(stockItemDto.getAmount());
        stockItemEntity.setCreatedBy(stockItemDto.getCreatedBy());
        stockItemEntity.setCreatedDate(stockItemDto.getCreatedDate());
        stockItemEntity.setModifiedBy(stockItemDto.getModifiedBy());
        stockItemEntity.setModifiedDate(stockItemDto.getModifiedDate());
        return stockItemEntity;
    }


//    public void updateEntityFromDto(StockDto stockDto, StockEntity stockEntity) {
//        if (stockDto == null || stockEntity == null) {
//            return;
//        }
//
//        stockEntity.setSupplierId(stockDto.getSupplierId());
//        stockEntity.setPurchaseBillNo(stockDto.getPurchaseBillNo());
//        stockEntity.setPurchaseDate(stockDto.getPurchaseDate());
//        stockEntity.setCreditPeriod(stockDto.getCreditPeriod());
//        stockEntity.setPaymentDueDate(stockDto.getPaymentDueDate());
//        stockEntity.setInvoiceAmount(stockDto.getInvoiceAmount() != null ? BigDecimal.valueOf(stockDto.getInvoiceAmount()) : null);
//        stockEntity.setTotalAmount(stockDto.getTotalAmount() != null ? BigDecimal.valueOf(stockDto.getTotalAmount()) : null);
//        stockEntity.setTotalGst(stockDto.getTotalGst() != null ? BigDecimal.valueOf(stockDto.getTotalGst()) : null);
//        stockEntity.setTotalDiscount(stockDto.getTotalDiscount() != null ? BigDecimal.valueOf(stockDto.getTotalDiscount()) : null);
//        stockEntity.setGrandTotal(stockDto.getGrandTotal() != null ? BigDecimal.valueOf(stockDto.getGrandTotal()) : null);
//        stockEntity.setPaymentStatus(stockDto.getPaymentStatus().toUpperCase());
//        stockEntity.setGoodStatus(stockDto.getGoodStatus().toUpperCase());
//
//        // Update StockItemEntities if provided in StockDto
//        if (stockDto.getStockItemDtos() != null && !stockDto.getStockItemDtos().isEmpty()) {
//            List<StockItemEntity> stockItemEntities = stockDto.getStockItemDtos().stream()
//                    .map(this::toEntity) // Reuse existing toEntity method for StockItemDto
//                    .collect(Collectors.toList());
//            stockItemEntities.forEach(item -> item.setStockEntity(stockEntity)); // Maintain bi-directional mapping
//            stockEntity.setStockItemEntities(stockItemEntities);
//        }
//    }

}
