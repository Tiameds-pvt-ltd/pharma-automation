package com.pharma.service.impl;

import com.pharma.dto.PharmacyDto;
import com.pharma.entity.*;
import com.pharma.mapper.PharmacistMapper;
import com.pharma.mapper.PharmacyMapper;
import com.pharma.repository.PharmacistRepository;
import com.pharma.dto.PharmacistDto;
import com.pharma.repository.PharmacyRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.PharmacyService;
import com.pharma.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Autowired
    private PharmacyMapper pharmacyMapper;

    @Autowired
    private PharmacistMapper pharmacistMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

        @Transactional
        @Override
        public PharmacyDto savePharmacy(PharmacyDto pharmacyDto, PharmacistDto pharmacistDto, User user) {
            user = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Pharmacy pharmacy = pharmacyMapper.toEntity(pharmacyDto);
            Pharmacist pharmacist = pharmacistMapper.toEntity(pharmacistDto);

            Optional<Pharmacist> existingPharmacist = pharmacistRepository.findByPharmacistName(pharmacist.getPharmacistName());
            if (existingPharmacist.isPresent()) {
                pharmacist = existingPharmacist.get();
            } else {
                pharmacist.setPharmacistId(UUID.randomUUID());
                pharmacist.setCreatedBy(user.getId());
                pharmacist.setCreatedDate(LocalDate.now());
                pharmacist = pharmacistRepository.save(pharmacist);
            }

            boolean pharmacyExists = pharmacyRepository.existsByPharmacyNameAndPharmacists(pharmacy.getPharmacyName(), pharmacist);

            if (!pharmacyExists) {
                pharmacy.setPharmacyId(UUID.randomUUID());
                pharmacy.setCreatedBy(user.getId());
                pharmacy.setCreatedDate(LocalDate.now());

                pharmacy.getPharmacists().add(pharmacist);
                pharmacist.getPharmacies().add(pharmacy);

                pharmacy = pharmacyRepository.save(pharmacy);
            }

            return pharmacyMapper.toDto(pharmacy);
        }


    @Override
    @Transactional
    public List<PharmacyDto> getAllPharmacies(Long createdById) {
        List<Pharmacy> pharmacies = pharmacyRepository.findAllByCreatedBy(createdById);
        return pharmacies.stream()
                .map(pharmacyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<PharmacistDto> getAllPharmacists(Long createdById) {
        List<Pharmacist> pharmacists = pharmacistRepository.findAllByCreatedBy(createdById);
        return pharmacists.stream()
                .map(pharmacistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PharmacyDto getPharmacyById(Long createdById, UUID pharmacyId) {
        Optional<Pharmacy> pharmacy = pharmacyRepository.findByPharmacyIdAndCreatedBy(pharmacyId, createdById);

        if (pharmacy.isEmpty()) {
            throw new RuntimeException("Pharmacy not found with ID: " + pharmacyId + " for user ID: " + createdById);
        }
        return pharmacyMapper.toDto(pharmacy.get());
    }

    @Override
    @Transactional
    public PharmacistDto getPharmacistById(Long createdById, UUID pharmacistId) {
        Optional<Pharmacist> pharmacist = pharmacistRepository.findByPharmacistIdAndCreatedBy(pharmacistId, createdById);

        if (pharmacist.isEmpty()) {
            throw new RuntimeException("Pharmacist not found with ID: " + pharmacistId + " for user ID: " + createdById);
        }
        return pharmacistMapper.toDto(pharmacist.get());
    }

    @Override
    @Transactional
    public void deletePharmacyById(Long createdById, UUID pharmacyId) {
        Optional<Pharmacy> pharmacyOptional = pharmacyRepository.findByPharmacyIdAndCreatedBy(pharmacyId, createdById);

        if (pharmacyOptional.isEmpty()) {
            throw new RuntimeException("Pharmacy not found with ID: " + pharmacyId + " for user ID: " + createdById);
        }

        Pharmacy pharmacy = pharmacyOptional.get();

        // Remove associations with pharmacists before deleting
        pharmacy.getPharmacists().forEach(pharmacist -> pharmacist.getPharmacies().remove(pharmacy));
        pharmacy.getPharmacists().clear();

        pharmacyRepository.delete(pharmacy);
    }

    @Override
    @Transactional
    public void deletePharmacistById(Long createdById, UUID pharmacistId) {
        Optional<Pharmacist> pharmacistOptional = pharmacistRepository.findByPharmacistIdAndCreatedBy(pharmacistId, createdById);

        if (pharmacistOptional.isEmpty()) {
            throw new RuntimeException("Pharmacist not found with ID: " + pharmacistId + " for user ID: " + createdById);
        }

        Pharmacist pharmacist = pharmacistOptional.get();

        // Remove associations with pharmacies before deleting
        pharmacist.getPharmacies().forEach(pharmacy -> pharmacy.getPharmacists().remove(pharmacist));
        pharmacist.getPharmacies().clear();

        pharmacistRepository.delete(pharmacist);
    }

//    @Override
//    @Transactional
//    public PharmacyDto updatePharmacy(Long modifiedById, UUID pharmacyId, PharmacyDto updatePharmacy) {
//        Optional<Pharmacy> pharmacyOptional = pharmacyRepository.findByPharmacyIdAndCreatedBy(pharmacyId, modifiedById);
//
//        if (pharmacyOptional.isEmpty()) {
//            throw new RuntimeException("Pharmacy not found with ID: " + pharmacyId + " for user ID: " + modifiedById);
//        }
//
//        Pharmacy pharmacy = pharmacyOptional.get();
//
//        // Update basic fields
//        pharmacy.setPharmacyName(updatePharmacy.getPharmacyName());
//        pharmacy.setAddress(updatePharmacy.getAddress());
//        pharmacy.setZipCode(updatePharmacy.getZipCode());
//        pharmacy.setGstNo(updatePharmacy.getGstNo());
//        pharmacy.setLicenseNo(updatePharmacy.getLicenseNo());
//        pharmacy.setLicenseProof(updatePharmacy.getLicenseProof());
//        pharmacy.setGstProof(updatePharmacy.getGstProof());
//
//        pharmacy.setModifiedBy(modifiedById);
//        pharmacy.setModifiedDate(LocalDate.now());
//
//        // ✅ FIX: Explicitly update the ManyToMany relationship
//        if (updatePharmacy.getPharmacistIds() != null) {
//            // Get current pharmacists
//            Set<Pharmacist> currentPharmacists = pharmacy.getPharmacists();
//
//            // Remove old associations
//            for (Pharmacist pharmacist : currentPharmacists) {
//                pharmacist.getPharmacies().remove(pharmacy);
//            }
//            pharmacy.getPharmacists().clear();
//
//            // Add new associations
//            Set<Pharmacist> updatedPharmacists = new HashSet<>();
//            for (UUID pharmacistId : updatePharmacy.getPharmacistIds()) {
//                Optional<Pharmacist> pharmacistOpt = pharmacistRepository.findById(pharmacistId);
//                if (pharmacistOpt.isPresent()) {
//                    Pharmacist pharmacist = pharmacistOpt.get();
//                    updatedPharmacists.add(pharmacist);
//                    pharmacist.getPharmacies().add(pharmacy); // ✅ Ensure bidirectional mapping
//                }
//            }
//
//            pharmacy.getPharmacists().addAll(updatedPharmacists);
//        }
//
//        // ✅ Save updated pharmacy with new relationships
//        Pharmacy updatedPharmacy = pharmacyRepository.save(pharmacy);
//        return pharmacyMapper.toDto(updatedPharmacy);
//    }

}
