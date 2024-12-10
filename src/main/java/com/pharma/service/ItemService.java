package com.pharma.service;



import com.pharma.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto);

    ItemDto getItemById(Integer itemId);

    List<ItemDto> getAllItem();

    ItemDto updateItem(Integer itemId, ItemDto updatedItem);

    void  deleteItem(Integer itemId);


}
