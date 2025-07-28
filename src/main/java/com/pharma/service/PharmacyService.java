package com.pharma.service;

import com.pharma.dto.PharmacyDto;
import com.pharma.entity.Pharmacy;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface PharmacyService {

//    PharmacyDto savePharmacy(PharmacyDto pharmacyDto, User user);
//
//    List<PharmacyDto> getAllPharmacies(Long createdById);
//
////    List<PharmacistDto> getAllPharmacists(Long createdById);
//
//    PharmacyDto getPharmacyById(Long createdById, Long pharmacyId);
//
////    PharmacistDto getPharmacistById(Long createdById, UUID pharmacistId);
//
//    void deletePharmacyById(Long createdById, Long pharmacyId);
//
////    void deletePharmacistById(Long createdById, UUID pharmacistId);

    PharmacyDto savePharmacy(PharmacyDto pharmacyDto, User user);

    List<PharmacyDto> getPharmaciesCreatedByUser(User user);

}
