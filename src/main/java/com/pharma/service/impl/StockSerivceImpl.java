package com.pharma.service.impl;

import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.InventoryEntity;
import com.pharma.entity.StockEntity;
import com.pharma.entity.StockItemEntity;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.StockMapper;


import com.pharma.repository.InventoryRepository;
import com.pharma.repository.StockRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockSerivceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public StockDto createStockAndAssociateWithUser(StockDto stockDto, User user) {
        StockEntity stockEntity = stockMapper.toEntity(stockDto);
        stockEntity.getUsers().add(user);
        user.getStockEntities().add(stockEntity);
        userRepository.save(user);

        // Loop through stock items to update or insert into ItemStockEntity
        for (StockItemEntity stockItem : stockEntity.getStockItemEntities()) {
            Optional<InventoryEntity> existingStock = inventoryRepository.findByItemId(stockItem.getItemId());

            if (existingStock.isPresent()) {
                // Update package quantity if item exists
                InventoryEntity inventory = existingStock.get();
                inventory.setPackageQuantity(inventory.getPackageQuantity() + stockItem.getPackageQuantity());
                inventoryRepository.save(inventory);
            } else {
                // Insert new row if item is new
                InventoryEntity newInventory  = new InventoryEntity();
                newInventory.setItemId(stockItem.getItemId());
                newInventory.setPackageQuantity(stockItem.getPackageQuantity());
                inventoryRepository.save(newInventory);
            }
        }


        return stockMapper.toDto(stockEntity);
    }


    @Override
    public List<StockDto> getAllStocks(String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        List<StockEntity> userStocks = new ArrayList<>(user.getStockEntities());
        return userStocks.stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StockDto getStockById(String userId, Long invId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        StockEntity stockEntity = stockRepository.findById(invId)
                .filter(stock -> stock.getUsers().contains(user))
                .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + invId + " for the user."));
        return stockMapper.toDto(stockEntity);
    }


    @Override
    public void deleteStock(Long invId, String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        StockEntity stockEntity = stockRepository.findById(invId)
                .filter(stock -> stock.getUsers().contains(user))
                .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + invId + " for the user."));
        stockEntity.getUsers().forEach(u -> u.getStockEntities().remove(stockEntity));
        userRepository.saveAll(stockEntity.getUsers());
        stockRepository.delete(stockEntity);
    }


    @Override
    public StockDto updateStock(Long invId, StockDto updatedStock) {
        StockEntity stockEntity = stockRepository.findById(invId).
                orElseThrow(() -> new ResourceNotFoundException("Stock does not exists with given ID :" + invId));

        stockEntity.setSupplierId(updatedStock.getSupplierId());
        stockEntity.setStore(updatedStock.getStore());
        stockEntity.setPurchaseBillNo(updatedStock.getPurchaseBillNo());
        stockEntity.setPurchaseDate(updatedStock.getPurchaseDate());
        stockEntity.setCreditPeriod(updatedStock.getCreditPeriod());
        stockEntity.setPaymentDueDate(updatedStock.getPaymentDueDate());
        stockEntity.setInvoiceAmount(BigDecimal.valueOf(updatedStock.getInvoiceAmount()));
        stockEntity.setTotalAmount(BigDecimal.valueOf(updatedStock.getTotalAmount()));
        stockEntity.setTotalGst(BigDecimal.valueOf(updatedStock.getTotalGst()));
        stockEntity.setTotalDiscount(BigDecimal.valueOf(updatedStock.getTotalDiscount()));
        stockEntity.setGrandTotal(BigDecimal.valueOf(updatedStock.getGrandTotal()));
        stockEntity.setPaymentStatus(updatedStock.getPaymentStatus());
        stockEntity.setGoodStatus(updatedStock.getGoodStatus());

        // Update StockItemEntities
        List<StockItemEntity> existingItems = stockEntity.getStockItemEntities();
        List<StockItemDto> updatedItems = updatedStock.getStockItemDtos();

        // Clear existing items and rebuild from updated DTO list
        existingItems.clear();
        for (StockItemDto itemDTO : updatedItems) {
            StockItemEntity itemEntity = stockMapper.toEntity(itemDTO);
            itemEntity.setStockEntity(stockEntity);  // Link each item to the StockEntity
            existingItems.add(itemEntity);
        }

        // Save the updated stock and associated items
        StockEntity updatedEntity = stockRepository.save(stockEntity);
        return stockMapper.toDto(updatedEntity);

    }

    public boolean isBillNoExists(Long supplierId, int year, String purchaseBillNo) {
        List<String> billNos = stockRepository.findBillNoBySupplierIdAndYear(supplierId, year, purchaseBillNo);
        return !billNos.isEmpty();
    }

}
