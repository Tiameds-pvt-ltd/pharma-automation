package com.pharma.service;


import com.pharma.dto.StockDto;

import java.util.List;

public interface StockService {

    StockDto createStock(StockDto stockDto);

    StockDto getStockById(Integer invId);

    List<StockDto> getAllStocks();

    StockDto updateStock(Integer invId, StockDto updatedStock);

    void deleteStock(Integer invId);


}
