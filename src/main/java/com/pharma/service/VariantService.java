package com.pharma.service;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.UnitDto;
import com.pharma.dto.VariantDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface VariantService {

    VariantDto createVariant(VariantDto variantDto, User user);

    List<VariantDto> getAllVariant(Long createdById);

    VariantDto getVariantById(Long createdById, UUID variantId);
//
//    VariantDto updateVariant(Long modifiedById, UUID variantId, VariantDto updateVariant);
//
    void deleteVariant(Long createdById, UUID variantId);
}
