package com.pharma.service.impl;

import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.StockEntity;
import com.pharma.entity.StockItemEntity;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.StockMapper;


import com.pharma.repository.StockRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockSerivceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private UserRepository userRepository;

//    @Override
//    public StockDto createStock(StockDto stockDto) {
//        StockEntity stockEntity = stockMapper.toEntity(stockDto);
//        stockEntity.getStockItemEntities().forEach(item -> item.setStockEntity(stockEntity));
//        StockEntity savedEntity = stockRepository.save(stockEntity);
//        return stockMapper.toDTO(savedEntity);
//    }
//
//
//    @Override
//    public List<StockDto> getAllStocks() {
//        return stockRepository.findAll().stream()
//                .map(stockMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public StockDto updateStock(Integer invId, StockDto updatedStock) {
//        return null;
//    }
//
//
//    @Override
//    public StockDto getStockById(Integer invId) {
//        StockEntity stockEntity = stockRepository.findById(invId)
//                .orElseThrow(() -> new RuntimeException("Stock not found"));
//        return stockMapper.toDTO(stockEntity);
//    }
//

//    @Override
//    public StockDto updateStock(Integer invId, StockDto updatedStock) {
//        StockEntity stockEntity = stockRepository.findById(invId).
//                orElseThrow(() -> new ResourceNotFoundException("Stock does not exists with given ID :" + invId));
//
//        stockEntity.setSupplierId(updatedStock.getSupplierId());
//        stockEntity.setStore(updatedStock.getStore());
//        stockEntity.setPurchaseBillNo(updatedStock.getPurchaseBillNo());
//        stockEntity.setPurchaseDate(updatedStock.getPurchaseDate());
//        stockEntity.setCreditPeriod(updatedStock.getCreditPeriod());
//        stockEntity.setPaymentDueDate(updatedStock.getPaymentDueDate());
//        stockEntity.setInvoiceAmount(updatedStock.getInvoiceAmount());
//        stockEntity.setTotalAmount(updatedStock.getTotalAmount());
//        stockEntity.setTotalGst(updatedStock.getTotalGst());
//        stockEntity.setTotalDiscount(updatedStock.getTotalDiscount());
//        stockEntity.setGrandTotal(updatedStock.getGrandTotal());
//        stockEntity.setPaymentStatus(updatedStock.getPaymentStatus());
//        stockEntity.setGoodStatus(updatedStock.getGoodStatus());
//
//        // Update StockItemEntities
//        List<StockItemEntity> existingItems = stockEntity.getStockItemEntities();
//        List<StockItemDto> updatedItems = updatedStock.getStockItemDtos();
//
//        // Clear existing items and rebuild from updated DTO list
//        existingItems.clear();
//        for (StockItemDto itemDTO : updatedItems) {
//            StockItemEntity itemEntity = stockMapper.toEntity(itemDTO);
//            itemEntity.setStockEntity(stockEntity);  // Link each item to the StockEntity
//            existingItems.add(itemEntity);
//        }
//
//        // Save the updated stock and associated items
//        StockEntity updatedEntity = stockRepository.save(stockEntity);
//        return stockMapper.toDTO(updatedEntity);
//
//    }

    @Override
    public StockDto createStock(StockDto stockDto) {
        return null;
    }

    @Override
    public StockDto getStockById(Integer invId) {
        return null;
    }

    @Override
    public List<StockDto> getAllStocks() {
        return List.of();
    }

    @Override
    public StockDto updateStock(Integer invId, StockDto updatedStock) {
        return null;
    }

    @Override
    public void deleteStock(Integer invId) {
        StockEntity stockEntity = stockRepository.findById(invId).
                orElseThrow(() -> new ResourceNotFoundException("Stock does not exists with given ID :" + invId));
        stockRepository.deleteById(invId);


    }

    @Override
    public StockDto createStockAndAssociateWithUser(StockDto stockDto, User user) {

        // Convert StockDto to StockEntity
        StockEntity stockEntity = new StockEntity();
        stockEntity.setSupplierId(stockDto.getSupplierId());
        stockEntity.setStore(stockDto.getStore());
        stockEntity.setPurchaseBillNo(stockDto.getPurchaseBillNo());
        stockEntity.setPurchaseDate(stockDto.getPurchaseDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate());
        stockEntity.setCreditPeriod(stockDto.getCreditPeriod());
        stockEntity.setPaymentDueDate(stockDto.getPaymentDueDate() != null
                ? stockDto.getPaymentDueDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate()
                : null);
        stockEntity.setInvoiceAmount(BigDecimal.valueOf(stockDto.getInvoiceAmount()));
        stockEntity.setTotalAmount(BigDecimal.valueOf(stockDto.getTotalAmount()));
        stockEntity.setTotalGst(BigDecimal.valueOf(stockDto.getTotalGst()));
        stockEntity.setTotalDiscount(BigDecimal.valueOf(stockDto.getTotalDiscount()));
        stockEntity.setGrandTotal(BigDecimal.valueOf(stockDto.getGrandTotal()));
        stockEntity.setPaymentStatus(StockEntity.PaymentStatus.valueOf(stockDto.getPaymentStatus().toUpperCase()));
        stockEntity.setGoodStatus(StockEntity.GoodStatus.valueOf(stockDto.getGoodStatus().toUpperCase()));

        // Handle StockItemDtos
        List<StockItemEntity> stockItemEntities = stockDto.getStockItemDtos().stream().map(itemDto -> {
            StockItemEntity stockItem = new StockItemEntity();
            stockItem.setItemId(itemDto.getItemId());
            stockItem.setBatchNo(itemDto.getBatchNo());
            stockItem.setPackageQuantity(itemDto.getPackageQuantity());
            stockItem.setExpiryDate(itemDto.getExpiryDate());
            stockItem.setFreeItem(itemDto.getFreeItem());
            stockItem.setDiscount(itemDto.getDiscount());
            stockItem.setGstPercentage(itemDto.getGstPercentage());
            stockItem.setGstAmount(itemDto.getGstAmount());
            stockItem.setAmount(itemDto.getAmount());
            stockItem.setStockEntity(stockEntity); // Associate stock item with stock entity
            return stockItem;
        }).toList();

        // Set stock items to stock entity
        stockEntity.setStockItemEntities(stockItemEntities);

        // Associate stock with user
        user.getStockEntities().add(stockEntity);

        // Save user and cascade save stock and stock items
        userRepository.save(user);

        // Convert saved StockEntity back to StockDto
        StockDto savedStockDto = new StockDto();
        savedStockDto.setInvId(stockEntity.getInvId());
        savedStockDto.setSupplierId(stockEntity.getSupplierId());
        savedStockDto.setStore(stockEntity.getStore());
        savedStockDto.setPurchaseBillNo(stockEntity.getPurchaseBillNo());
        savedStockDto.setPurchaseDate(Date.from(stockEntity.getPurchaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        savedStockDto.setCreditPeriod(stockEntity.getCreditPeriod());
        savedStockDto.setPaymentDueDate(stockEntity.getPaymentDueDate() != null
                ? Date.from(stockEntity.getPaymentDueDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
                : null);
        savedStockDto.setInvoiceAmount(stockEntity.getInvoiceAmount().intValue());
        savedStockDto.setTotalAmount(stockEntity.getTotalAmount().doubleValue());
        savedStockDto.setTotalGst(stockEntity.getTotalGst().doubleValue());
        savedStockDto.setTotalDiscount(stockEntity.getTotalDiscount().doubleValue());
        savedStockDto.setGrandTotal(stockEntity.getGrandTotal().doubleValue());
        savedStockDto.setPaymentStatus(stockEntity.getPaymentStatus().name());
        savedStockDto.setGoodStatus(stockEntity.getGoodStatus().name());

        // Convert stock items back to StockItemDtos
        List<StockItemDto> savedStockItemDtos = stockEntity.getStockItemEntities().stream().map(item -> {
            StockItemDto itemDto = new StockItemDto();
            itemDto.setStockId(item.getId());
            itemDto.setItemId(item.getItemId());
            itemDto.setBatchNo(item.getBatchNo());
            itemDto.setPackageQuantity(item.getPackageQuantity());
            itemDto.setExpiryDate(item.getExpiryDate());
            itemDto.setFreeItem(item.getFreeItem());
            itemDto.setDiscount(item.getDiscount());
            itemDto.setGstPercentage(item.getGstPercentage());
            itemDto.setGstAmount(item.getGstAmount().doubleValue());
            itemDto.setAmount(item.getAmount().doubleValue());
            return itemDto;
        }).toList();

        savedStockDto.setStockItemDtos(savedStockItemDtos);

        return savedStockDto;
    }


}
