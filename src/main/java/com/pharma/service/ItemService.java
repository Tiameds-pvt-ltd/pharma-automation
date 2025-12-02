package com.pharma.service;

import com.pharma.dto.ItemDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, User user);

    List<ItemDto> getAllItem(Long pharmacyId, User user);

    ItemDto getItemById(Long pharmacyId, UUID itemId, User user);

    ItemDto updateItem(Long pharmacyId, UUID itemId, ItemDto updatedItem, User user);

    void  deleteItem(Long pharmacyId, UUID itemId, User user);


}
