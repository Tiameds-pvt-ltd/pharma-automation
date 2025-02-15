package com.pharma.mapper;

import com.pharma.dto.InventoryDto;
import com.pharma.entity.InventoryEntity;

public class InventoryMapper {

    public static InventoryDto mapToInventoryDto(InventoryEntity inventoryEntity){
        return new InventoryDto(
                inventoryEntity.getInvItemId(),
                inventoryEntity.getItemId(),
                inventoryEntity.getPackageQuantity()
        );
    }

    public static InventoryEntity mapToInventoryEntity(InventoryDto inventoryDto){
        return  new InventoryEntity(
                inventoryDto.getInvItemId(),
                inventoryDto.getItemId(),
                inventoryDto.getPackageQuantity()

                );
    }
}

