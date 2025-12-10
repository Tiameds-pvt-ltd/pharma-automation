package com.pharma.service;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.UnitDto;
import com.pharma.dto.VariantDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface VariantService {

    VariantDto createVariant(VariantDto variantDto, User user);

    List<VariantDto> getAllVariants(Long pharmacyId, User user);

    VariantDto getVariantById(Long pharmacyId, UUID variantId, User user);

    VariantDto updateVariant(Long pharmacyId, UUID variantId, VariantDto updateVariant, User user);

    void deleteVariant(Long pharmacyId, UUID variantId, User user);
}
