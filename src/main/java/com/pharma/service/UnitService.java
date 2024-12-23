package com.pharma.service;

import com.pharma.dto.UnitDto;

import java.util.List;

public interface UnitService {

    UnitDto createUnit(UnitDto unitDto);

    List<UnitDto> getAllUnit();

    UnitDto updateUnit(Long unitId, UnitDto updateUnit);

    void deleteUnit(Long unitId);
}
