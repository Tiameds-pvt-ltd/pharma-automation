package com.pharma.service;


import com.pharma.dto.StockDto;
import com.pharma.entity.User;

import java.util.List;

public interface StockService {

    StockDto createStock(StockDto stockDto);

    StockDto getStockById(Integer invId);

    List<StockDto> getAllStocks();

    StockDto updateStock(Integer invId, StockDto updatedStock);

    void deleteStock(Integer invId);

    StockDto createStockAndAssociateWithUser(StockDto stockDto, User user);
}
