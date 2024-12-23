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
import java.util.ArrayList;
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

    @Override
    public StockDto createStockAndAssociateWithUser(StockDto stockDto, User user) {
        // Convert StockDto to StockEntity using the mapper
        StockEntity stockEntity = stockMapper.toEntity(stockDto);

        // Associate stock entity with the user
        stockEntity.getUsers().add(user);
        user.getStockEntities().add(stockEntity);

        // Save the user, which cascades the save for stock and stock items
        userRepository.save(user);

        // Convert the saved StockEntity back to StockDto using the mapper
        return stockMapper.toDto(stockEntity);
    }


    @Override
    public List<StockDto> getAllStocks(String userId) {
        // Fetch the user entity using the userId from the token
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Retrieve all stocks associated with the user (convert Set to List)
        List<StockEntity> userStocks = new ArrayList<>(user.getStockEntities());

        // Convert StockEntities to StockDtos using the stockMapper
        return userStocks.stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StockDto getStockById(String userId, Long invId) {
        // Fetch the user entity using the userId from the token
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Fetch the stock entity by ID and ensure it is associated with the user
        StockEntity stockEntity = stockRepository.findById(Math.toIntExact(invId))
                .filter(stock -> stock.getUsers().contains(user))
                .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + invId + " for the user."));

        // Convert the StockEntity to StockDto using the mapper
        return stockMapper.toDto(stockEntity);
    }


    @Override
    public void deleteStock(Long invId, String userId) {
        // Fetch the user entity using the userId from the token
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Fetch the stock entity and ensure it is associated with the user
        StockEntity stockEntity = stockRepository.findById(Math.toIntExact(invId))
                .filter(stock -> stock.getUsers().contains(user))
                .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + invId + " for the user."));

        // Remove associations with users to avoid FK constraint violations
        stockEntity.getUsers().forEach(u -> u.getStockEntities().remove(stockEntity));
        userRepository.saveAll(stockEntity.getUsers());

        // Delete the stock entity
        stockRepository.delete(stockEntity);
    }


//    @Override
//    public StockDto updateStock(Integer invId, StockDto updatedStock) {
//        // Fetch the existing stock entity
//        StockEntity existingStockEntity = stockRepository.findById(invId)
//                .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + invId));
//
//        // Map updated fields from DTO to entity
//        stockMapper.updateEntityFromDto(updatedStock, existingStockEntity);
//
//        // Handle stock item updates
//        List<StockItemEntity> updatedStockItems = updatedStock.getStockItemDtos()
//                .stream()
//                .map(stockItemMapper::toEntity)
//                .peek(item -> item.setStockEntity(existingStockEntity)) // Associate with parent stock
//                .toList();
//
//        existingStockEntity.getStockItemEntities().clear();
//        existingStockEntity.getStockItemEntities().addAll(updatedStockItems);
//
//        // Save the updated entity
//        stockRepository.save(existingStockEntity);
//
//        // Convert the updated entity back to DTO
//        return stockMapper.toDto(existingStockEntity);
//    }

}
