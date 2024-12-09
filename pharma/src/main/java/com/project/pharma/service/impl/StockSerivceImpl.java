package com.project.pharma.service.impl;

import com.project.pharma.dto.StockDto;

import com.project.pharma.dto.StockItemDto;
import com.project.pharma.entity.StockEntity;
import com.project.pharma.entity.StockItemEntity;
import com.project.pharma.exception.ResourceNotFoundException;
import com.project.pharma.mapper.StockMapper;
import com.project.pharma.repository.StockRepository;
import com.project.pharma.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockSerivceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Override
    public StockDto createStock(StockDto stockDto) {
        StockEntity stockEntity = stockMapper.toEntity(stockDto);
        stockEntity.getStockItemEntities().forEach(item -> item.setStockEntity(stockEntity));
        StockEntity savedEntity = stockRepository.save(stockEntity);
        return stockMapper.toDTO(savedEntity);
    }

    @Override
    public List<StockDto> getAllStocks() {
        return stockRepository.findAll().stream()
                .map(stockMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public StockDto getStockById(Integer invId) {
        StockEntity stockEntity = stockRepository.findById(invId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        return stockMapper.toDTO(stockEntity);
    }


    @Override
    public StockDto updateStock(Integer invId, StockDto updatedStock) {
        StockEntity stockEntity = stockRepository.findById(invId).
                orElseThrow(() -> new ResourceNotFoundException("Stock does not exists with given ID :" + invId));

        stockEntity.setSupplierId(updatedStock.getSupplierId());
        stockEntity.setStore(updatedStock.getStore());
        stockEntity.setPurchaseBillNo(updatedStock.getPurchaseBillNo());
        stockEntity.setPurchaseDate(updatedStock.getPurchaseDate());
        stockEntity.setCreditPeriod(updatedStock.getCreditPeriod());
        stockEntity.setPaymentDueDate(updatedStock.getPaymentDueDate());
        stockEntity.setInvoiceAmount(updatedStock.getInvoiceAmount());
        stockEntity.setTotalAmount(updatedStock.getTotalAmount());
        stockEntity.setTotalGst(updatedStock.getTotalGst());
        stockEntity.setTotalDiscount(updatedStock.getTotalDiscount());
        stockEntity.setGrandTotal(updatedStock.getGrandTotal());
        stockEntity.setPaymentStatus(updatedStock.getPaymentStatus());
        stockEntity.setGoodStatus(updatedStock.getGoodStatus());

        // Update StockItemEntities
        List<StockItemEntity> existingItems = stockEntity.getStockItemEntities();
        List<StockItemDto> updatedItems = updatedStock.getStockItems();

        // Clear existing items and rebuild from updated DTO list
        existingItems.clear();
        for (StockItemDto itemDTO : updatedItems) {
            StockItemEntity itemEntity = stockMapper.toEntity(itemDTO);
            itemEntity.setStockEntity(stockEntity);  // Link each item to the StockEntity
            existingItems.add(itemEntity);
        }

        // Save the updated stock and associated items
        StockEntity updatedEntity = stockRepository.save(stockEntity);
        return stockMapper.toDTO(updatedEntity);

    }


    @Override
    public void deleteStock(Integer invId) {
        StockEntity stockEntity = stockRepository.findById(invId).
                orElseThrow(() -> new ResourceNotFoundException("Stock does not exists with given ID :" + invId));
        stockRepository.deleteById(invId);


    }
}
