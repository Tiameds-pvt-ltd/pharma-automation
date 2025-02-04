package com.pharma.mapper;

import com.pharma.dto.UnitDto;
import com.pharma.entity.UnitEntity;

public class UnitMapper {

    public static UnitDto mapToDto(UnitEntity unitEntity){
        return new UnitDto(
                unitEntity.getUnitId(),
                unitEntity.getUnitName(),
                unitEntity.getUnitDate()
        );
    }

    public static UnitEntity mapToEntity(UnitDto unitDto){
        return new UnitEntity(
                unitDto.getUnitId(),
                unitDto.getUnitName(),
                unitDto.getUnitDate()
        );
    }
}
