package com.pharma.service;

import com.pharma.dto.UnitDto;
import com.pharma.dto.VariantDto;
import com.pharma.entity.User;
import java.util.List;
import java.util.UUID;

public interface UnitService {

    UnitDto createUnit(UnitDto unitDto, User user);

    List<UnitDto> getAllUnit(Long createdById);

    UnitDto getUnitById(Long createdById, UUID unitId);

    UnitDto updateUnit(Long modifiedById, UUID unitId, UnitDto updateUnit);

    void deleteUnit(Long createdById, UUID unitId);
}
