package com.project.pharma.service.impl;

import com.project.pharma.dto.ItemDto;
import com.project.pharma.entity.ItemEntity;
import com.project.pharma.exception.ResourceNotFoundException;
import com.project.pharma.mapper.ItemMapper;
import com.project.pharma.repository.ItemRepository;
import com.project.pharma.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto) {
        ItemEntity itemEntity = ItemMapper.mapToItemEntity(itemDto);
        ItemEntity saveItem = itemRepository.save(itemEntity);
        return ItemMapper.mapToItemDto(saveItem);
    }

    @Override
    public ItemDto getItemById(Integer itemId) {
        ItemEntity itemEntity = itemRepository.findById(itemId).
                orElseThrow(() -> new ResourceNotFoundException("Item does not exists with given ID :" + itemId));
        return ItemMapper.mapToItemDto(itemEntity);
    }

    @Override
    public List<ItemDto> getAllItem() {
        List<ItemEntity> itemEntities = itemRepository.findAll();
        return itemEntities.stream().map((itemEntity) -> ItemMapper.mapToItemDto(itemEntity)).collect(Collectors.toList());
    }


    @Override
    public ItemDto updateItem(Integer itemId, ItemDto updatedItem) {
        ItemEntity itemEntity = itemRepository.findById(itemId).
                orElseThrow(() -> new ResourceNotFoundException("Item does not exists with given ID :" + itemId));

        itemEntity.setItemName(updatedItem.getItemName());
        itemEntity.setPurchaseUnit(updatedItem.getPurchaseUnit());
        itemEntity.setUnitType(updatedItem.getUnitType());
        itemEntity.setManufacturer(updatedItem.getManufacturer());
        itemEntity.setPurchasePrice(updatedItem.getPurchasePrice());
        itemEntity.setMrpSalePrice(updatedItem.getMrpSalePrice());
        itemEntity.setPurchasePricePerUnit(updatedItem.getPurchasePricePerUnit());
        itemEntity.setMrpSalePricePerUnit(updatedItem.getMrpSalePricePerUnit());
        itemEntity.setGstPercentage(updatedItem.getGstPercentage());
        itemEntity.setHsnNo(updatedItem.getHsnNo());
        itemEntity.setConsumables(updatedItem.getConsumables());

        ItemEntity updatedItemObj = itemRepository.save(itemEntity);
        return ItemMapper.mapToItemDto(updatedItemObj);
    }


    @Override
    public void deleteItem(Integer itemId) {
        ItemEntity itemEntity = itemRepository.findById(itemId).
                orElseThrow(() -> new ResourceNotFoundException("Item does not exists with given ID :" + itemId));
        itemRepository.deleteById(itemId);
    }
}
