package com.pharma.service;


import com.pharma.dto.StockDto;
import com.pharma.entity.User;

import java.util.List;

public interface StockService {

    StockDto createStockAndAssociateWithUser(StockDto stockDto, User user);

    List<StockDto> getAllStocks(String userId);

    StockDto getStockById(String userId, Long invId);

//    StockDto updateStock(Long invId, StockDto stockDto, String userId);

    void deleteStock(Long invId, String userId);


}
