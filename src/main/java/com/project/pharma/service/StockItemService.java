package com.project.pharma.service;

import com.project.pharma.dto.StockItemDto;

import java.util.List;

public interface StockItemService {

    StockItemDto createStockItem(StockItemDto stockItemDto);

    StockItemDto getStockItemById(Integer stockId);

    List<StockItemDto> getAllStockItem();

    StockItemDto updateStockItem(Integer stockId, StockItemDto updatedStockItem);

    void deleteStockItem(Integer stockId);


}

