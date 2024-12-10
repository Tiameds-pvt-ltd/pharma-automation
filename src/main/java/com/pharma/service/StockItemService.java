package com.pharma.service;

import com.pharma.dto.StockItemDto;

import java.util.List;

public interface StockItemService {

    StockItemDto createStockItem(StockItemDto stockItemDto);

    StockItemDto getStockItemById(Integer stockId);

    List<StockItemDto> getAllStockItem();

    StockItemDto updateStockItem(Integer stockId, StockItemDto updatedStockItem);

    void deleteStockItem(Integer stockId);


}

