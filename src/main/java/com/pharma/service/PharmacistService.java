//package com.pharma.service;
//
//import com.pharma.dto.PharmacistDto;
//import com.pharma.entity.User;
//
//import java.util.List;
//import java.util.UUID;
//
//public interface PharmacistService {
//
//    PharmacistDto savePharmacist(PharmacistDto pharmacistDto, User user);
//    List<PharmacistDto> getAllPharmacists(Long createdById);
//    PharmacistDto getPharmacistById(Long createdById, UUID pharmacistId);
//    void deletePharmacistById(Long createdById, UUID pharmacistId);
//
//    // 🚀 Many-to-Many Relationship Management (Assigning Pharmacies)
//    PharmacistDto assignPharmaciesToPharmacist(UUID pharmacistId, List<UUID> pharmacyIds, User user);
//    PharmacistDto removePharmacyFromPharmacist(UUID pharmacistId, UUID pharmacyId, User user);
//}
