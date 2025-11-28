package com.pharma.service.impl;

import com.pharma.dto.VariantDto;
import com.pharma.entity.UnitEntity;
import com.pharma.entity.User;
import com.pharma.entity.VariantEntity;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.VariantMapper;
import com.pharma.repository.VariantRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.VariantService;
import com.pharma.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VariantServiceImpl implements VariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private VariantMapper variantMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;


    @Transactional
    @Override
    public VariantDto createVariant(VariantDto variantDto, User user) {
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        VariantEntity variantEntity = variantMapper.toEntity(variantDto);
        variantEntity.setVariantId(UUID.randomUUID());
        variantEntity.setCreatedBy(user.getId());
        variantEntity.setCreatedDate(LocalDate.now());

        if (variantEntity.getUnitEntities() != null) {
            for (UnitEntity unit : variantEntity.getUnitEntities()) {
                unit.setUnitId(UUID.randomUUID());
                unit.setCreatedBy(user.getId());
                unit.setCreatedDate(LocalDate.now());
                unit.setVariantEntity(variantEntity);
            }
        }

        VariantEntity savedEntity = variantRepository.save(variantEntity);
        return variantMapper.toDto(savedEntity);
    }


    @Override
    @Transactional(readOnly = true)
    public List<VariantDto> getAllVariants() {
        List<VariantEntity> entities = variantRepository.findAll();
        return entities.stream()
                .map(variantMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public VariantDto getVariantById(Long createdById, UUID variantId) {
        Optional<VariantEntity> variantEntityOptional = variantRepository.findByVariantIdAndCreatedBy(variantId, createdById);

        if (variantEntityOptional.isEmpty()) {
            throw new RuntimeException("Variant not found with ID: " + variantId + " for user ID: " + createdById);
        }

        return variantMapper.toDto(variantEntityOptional.get());
    }

    @Override
    public VariantDto updateVariant(Long modifiedById, UUID variantId, VariantDto updateVariant) {
        return null;
    }

    @Override
    @Transactional
    public void deleteVariant(Long createdById, UUID variantId) {
        Optional<VariantEntity> variantEntityOptional = variantRepository.findByVariantIdAndCreatedBy(variantId, createdById);

        if (variantEntityOptional.isEmpty()) {
            throw new RuntimeException("Variant not found with ID: " + variantId + " for user ID: " + createdById);
        }

        variantRepository.delete(variantEntityOptional.get());
    }
}
