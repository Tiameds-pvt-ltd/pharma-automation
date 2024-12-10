package com.pharma.mapper;


import com.pharma.dto.ItemDto;
import com.pharma.entity.ItemEntity;


public class ItemMapper {

    public static ItemDto mapToItemDto(ItemEntity itemEntity){
        return new ItemDto(
                itemEntity.getItemId(),
                itemEntity.getItemName(),
                itemEntity.getPurchaseUnit(),
                itemEntity.getUnitType(),
                itemEntity.getManufacturer(),
                itemEntity.getPurchasePrice(),
                itemEntity.getMrpSalePrice(),
                itemEntity.getPurchasePricePerUnit(),
                itemEntity.getMrpSalePricePerUnit(),
                itemEntity.getGstPercentage(),
                itemEntity.getHsnNo(),
                itemEntity.getConsumables()


        );
    }

    public static ItemEntity mapToItemEntity(ItemDto itemDto){
        return new ItemEntity(
                itemDto.getItemId(),
                itemDto.getItemName(),
                itemDto.getPurchaseUnit(),
                itemDto.getUnitType(),
                itemDto.getManufacturer(),
                itemDto.getPurchasePrice(),
                itemDto.getMrpSalePrice(),
                itemDto.getPurchasePricePerUnit(),
                itemDto.getMrpSalePricePerUnit(),
                itemDto.getGstPercentage(),
                itemDto.getHsnNo(),
                itemDto.getConsumables()

        );

    }
}
