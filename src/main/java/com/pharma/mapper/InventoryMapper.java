package com.pharma.mapper;

import com.pharma.dto.InventoryDto;
import com.pharma.entity.InventoryEntity;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryDto mapToDto(InventoryEntity inventoryEntity){
        return new InventoryDto(
                inventoryEntity.getInvItemId(),
                inventoryEntity.getItemId(),
                inventoryEntity.getPackageQuantity(),
                inventoryEntity.getCreatedBy(),
                inventoryEntity.getCreatedDate(),
                inventoryEntity.getModifiedBy(),
                inventoryEntity.getModifiedDate()
        );
    }

    public static InventoryEntity mapToEntity(InventoryDto inventoryDto){
        return  new InventoryEntity(
                inventoryDto.getInvItemId(),
                inventoryDto.getItemId(),
                inventoryDto.getPackageQuantity(),
                inventoryDto.getCreatedBy(),
                inventoryDto.getCreatedDate(),
                inventoryDto.getModifiedBy(),
                inventoryDto.getModifiedDate()

                );
    }
}

