package com.project.pharma.service;

import com.project.pharma.dto.ItemDto;
import com.project.pharma.dto.StockDto;

import java.util.List;

public interface StockService {

    StockDto createStock(StockDto stockDto);

    StockDto getStockById(Integer invId);

    List<StockDto> getAllStocks();

    StockDto updateStock(Integer invId, StockDto updatedStock);

    void  deleteStock(Integer invId);


}
