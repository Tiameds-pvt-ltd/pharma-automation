package com.pharma.service;

import com.pharma.dto.PharmacistDto;
import com.pharma.dto.PharmacyDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface PharmacyService {

    PharmacyDto savePharmacy(PharmacyDto pharmacyDto, PharmacistDto pharmacistDto, User user);

    List<PharmacyDto> getAllPharmacies(Long createdById);

    List<PharmacistDto> getAllPharmacists(Long createdById);

    PharmacyDto getPharmacyById(Long createdById, UUID pharmacyId);

    PharmacistDto getPharmacistById(Long createdById, UUID pharmacistId);

    void deletePharmacyById(Long createdById, UUID pharmacyId);

    void deletePharmacistById(Long createdById, UUID pharmacistId);

}
