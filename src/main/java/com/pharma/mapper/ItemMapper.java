package com.pharma.mapper;

import org.springframework.stereotype.Component;


import com.pharma.dto.ItemDto;
import com.pharma.entity.ItemEntity;

@Component
public class ItemMapper {

    public ItemDto mapToItemDto(ItemEntity itemEntity){
        return new ItemDto(
                itemEntity.getItemId(),
                itemEntity.getItemName(),
                itemEntity.getPurchaseUnit(),
                itemEntity.getUnitId(),
                itemEntity.getVariantId(),
                itemEntity.getManufacturer(),
                itemEntity.getPurchasePrice(),
                itemEntity.getMrpSalePrice(),
                itemEntity.getPurchasePricePerUnit(),
                itemEntity.getMrpSalePricePerUnit(),
                itemEntity.getCgstPercentage(),
                itemEntity.getSgstPercentage(),
                itemEntity.getHsnNo(),
                itemEntity.getConsumables(),
                itemEntity.getCreatedBy(),
                itemEntity.getCreatedDate(),
                itemEntity.getModifiedBy(),
                itemEntity.getModifiedDate()


        );
    }

    public static ItemEntity mapToItemEntity(ItemDto itemDto){
        return new ItemEntity(
                itemDto.getItemId(),
                itemDto.getItemName(),
                itemDto.getPurchaseUnit(),
                itemDto.getUnitId(),
                itemDto.getVariantId(),
                itemDto.getManufacturer(),
                itemDto.getPurchasePrice(),
                itemDto.getMrpSalePrice(),
                itemDto.getPurchasePricePerUnit(),
                itemDto.getMrpSalePricePerUnit(),
                itemDto.getCgstPercentage(),
                itemDto.getSgstPercentage(),
                itemDto.getHsnNo(),
                itemDto.getConsumables(),
                itemDto.getCreatedBy(),
                itemDto.getCreatedDate(),
                itemDto.getModifiedBy(),
                itemDto.getModifiedDate()

        );

    }
}
