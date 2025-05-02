//package com.pharma.service.impl;
//
//import com.pharma.dto.UnitDto;
//import com.pharma.dto.VariantDto;
//import com.pharma.entity.*;
//import com.pharma.exception.ResourceNotFoundException;
//import com.pharma.mapper.UnitMapper;
//import com.pharma.repository.UnitRepository;
//import com.pharma.repository.auth.UserRepository;
//import com.pharma.service.UnitService;
//import com.pharma.utils.JwtUtil;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@AllArgsConstructor
//public class UnitServiceImpl implements UnitService {
//
//    @Autowired
//    private UnitRepository unitRepository;
//
//    @Autowired
//    private UnitMapper unitMapper;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Transactional
//    @Override
//    public UnitDto createUnit(UnitDto unitDto, User user) {
//        user = userRepository.findById(user.getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UnitEntity unitEntity = unitMapper.mapToEntity(unitDto);
//        unitEntity.setUnitId(UUID.randomUUID());
//        unitEntity.setCreatedBy(user.getId());
//        unitEntity.setCreatedDate(LocalDate.now());
//
//        UnitEntity savedUnit = unitRepository.save(unitEntity);
//        return unitMapper.mapToDto(savedUnit);
//    }
//
//    @Transactional
//    @Override
//    public List<UnitDto> getAllUnit(Long createdById) {
//        List<UnitEntity> units = unitRepository.findAllByCreatedBy(createdById);
//        return units.stream()
//                .map(unitMapper::mapToDto)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    @Override
//    public UnitDto getUnitById(Long createdById, UUID unitId) {
//        Optional<UnitEntity> unitEntity = unitRepository.findByUnitIdAndCreatedBy(unitId, createdById);
//
//        if (unitEntity.isEmpty()) {
//            throw new ResourceNotFoundException("Unit not found with ID: " + unitId + " for user ID: " + createdById);
//        }
//        return unitMapper.mapToDto(unitEntity.get());
//    }
//
//    @Transactional
//    @Override
//    public UnitDto updateUnit(Long modifiedById, UUID unitId, UnitDto updateUnit) {
//        Optional<UnitEntity> unitEntityOptional = unitRepository.findById(unitId);
//
//        if (unitEntityOptional.isEmpty()) {
//            throw new ResourceNotFoundException("Unit not found with ID: " + unitEntityOptional);
//        }
//
//        UnitEntity unitEntity = unitEntityOptional.get();
//
//        unitEntity.setUnitName(updateUnit.getUnitName());
//
//        unitEntity.setModifiedBy(modifiedById);
//        unitEntity.setModifiedDate(LocalDate.now());
//
//        UnitEntity updatedUnit = unitRepository.save(unitEntity);
//        return unitMapper.mapToDto(updatedUnit);
//    }
//
//    @Transactional
//    @Override
//    public void deleteUnit(Long createdById, UUID unitId) {
//        Optional<UnitEntity> unitEntity = unitRepository.findByUnitIdAndCreatedBy(unitId, createdById);
//
//        if (unitEntity.isEmpty()) {
//            throw new ResourceNotFoundException("Unit not found with ID: " + unitId + " for user ID: " + createdById);
//        }
//
//        unitRepository.delete(unitEntity.get());
//    }
//
//
//}
