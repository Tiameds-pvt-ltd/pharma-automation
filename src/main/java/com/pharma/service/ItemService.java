package com.pharma.service;

import com.pharma.dto.ItemDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, User user);

    List<ItemDto> getAllItem(Long createdById);

    ItemDto getItemById(Long createdById, UUID itemId);

    ItemDto updateItem(Long modifiedById, UUID itemId, ItemDto updatedItem);

    void  deleteItem(Long createdById, UUID itemId);


}
