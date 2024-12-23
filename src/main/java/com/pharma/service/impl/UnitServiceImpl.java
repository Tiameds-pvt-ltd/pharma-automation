package com.pharma.service.impl;

import com.pharma.dto.UnitDto;
import com.pharma.entity.SupplierEntity;
import com.pharma.entity.UnitEntity;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.UnitMapper;
import com.pharma.repository.UnitRepository;
import com.pharma.service.UnitService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UnitServiceImpl implements UnitService {

    private UnitRepository unitRepository;

    @Override
    public UnitDto createUnit(UnitDto unitDto) {
        UnitEntity unitEntity = UnitMapper.mapToEntity(unitDto);
        UnitEntity saveUnit = unitRepository.save(unitEntity);
        return UnitMapper.mapToDto(saveUnit);
    }

    @Override
    public List<UnitDto> getAllUnit() {
       List<UnitEntity> unitEntities = unitRepository.findAll();
       return unitEntities.stream().map((unitEntity) -> UnitMapper.mapToDto(unitEntity)).collect(Collectors.toList());
    }

    @Override
    public UnitDto updateUnit(Long unitId, UnitDto updateUnit) {
        UnitEntity unitEntity = unitRepository.findById(unitId).
                orElseThrow(() -> new ResourceNotFoundException("Unit does not exists with the given ID : " + unitId));

        unitEntity.setUnitName(updateUnit.getUnitName());

        UnitEntity updateUintObj = unitRepository.save(unitEntity);
        return UnitMapper.mapToDto(updateUintObj);
    }

    @Override
    public void deleteUnit(Long unitId) {
        UnitEntity unitEntity = unitRepository.findById(unitId).
                orElseThrow(() -> new ResourceNotFoundException("Unit does not exists with the given ID : " + unitId));
        unitRepository.deleteById(unitId);

    }

}
