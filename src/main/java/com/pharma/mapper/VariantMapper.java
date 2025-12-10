package com.pharma.mapper;

import com.pharma.dto.UnitDto;
import com.pharma.dto.VariantDto;
import com.pharma.entity.UnitEntity;
import com.pharma.entity.VariantEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VariantMapper {

    public VariantDto toDto(VariantEntity variantEntity) {
        if (variantEntity == null) {
            return null;
        }

        VariantDto variantDto = new VariantDto();
        variantDto.setVariantId(variantEntity.getVariantId());
        variantDto.setVariantName(variantEntity.getVariantName());
        variantDto.setPharmacyId(variantEntity.getPharmacyId());
        variantDto.setCreatedBy(variantEntity.getCreatedBy());
        variantDto.setCreatedDate(variantEntity.getCreatedDate());
        variantDto.setModifiedBy(variantEntity.getModifiedBy());
        variantDto.setModifiedDate(variantEntity.getModifiedDate());

        List<UnitDto> unitDtos = variantEntity.getUnitEntities().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        variantDto.setUnitDtos(unitDtos);

        return variantDto;
    }

    public VariantEntity toEntity(VariantDto variantDto) {
        if (variantDto == null) {
            return null;
        }

        VariantEntity variantEntity = new VariantEntity();
        variantEntity.setVariantId(variantDto.getVariantId());
        variantEntity.setVariantName(variantDto.getVariantName());
        variantEntity.setPharmacyId(variantDto.getPharmacyId());
        variantEntity.setCreatedBy(variantDto.getCreatedBy());
        variantEntity.setCreatedDate(variantDto.getCreatedDate());
        variantEntity.setModifiedBy(variantDto.getModifiedBy());
        variantEntity.setModifiedDate(variantDto.getModifiedDate());

        List<UnitEntity> unitEntities = variantDto.getUnitDtos().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        unitEntities.forEach(unit -> unit.setVariantEntity(variantEntity)); // Maintain bi-directional mapping
        variantEntity.setUnitEntities(unitEntities);

        return variantEntity;
    }

    private UnitDto toDto(UnitEntity unitEntity) {
        if (unitEntity == null) {
            return null;
        }

        UnitDto unitDto = new UnitDto();
        unitDto.setUnitId(unitEntity.getUnitId());
        unitDto.setUnitName(unitEntity.getUnitName());
        unitDto.setCreatedBy(unitEntity.getCreatedBy());
        unitDto.setCreatedDate(unitEntity.getCreatedDate());
        unitDto.setModifiedBy(unitEntity.getModifiedBy());
        unitDto.setModifiedDate(unitEntity.getModifiedDate());

        return unitDto;
    }

    private UnitEntity toEntity(UnitDto unitDto) {
        if (unitDto == null) {
            return null;
        }

        UnitEntity unitEntity = new UnitEntity();
        unitEntity.setUnitId(unitDto.getUnitId());
        unitEntity.setUnitName(unitDto.getUnitName());
        unitEntity.setCreatedBy(unitDto.getCreatedBy());
        unitEntity.setCreatedDate(unitDto.getCreatedDate());
        unitEntity.setModifiedBy(unitDto.getModifiedBy());
        unitEntity.setModifiedDate(unitDto.getModifiedDate());

        return unitEntity;
    }
}
