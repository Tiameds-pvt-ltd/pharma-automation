package com.pharma.service.impl;

import com.pharma.dto.PharmacyDto;
import com.pharma.entity.Pharmacy;
import com.pharma.entity.User;
import com.pharma.mapper.PharmacyMapper;
import com.pharma.repository.PharmacyRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.PharmacyService;
import com.pharma.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PharmacyMapper pharmacyMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public PharmacyDto savePharmacy(PharmacyDto pharmacyDto, User user) {

        // 1️⃣ Creator (persistent)
        User creator = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2️⃣ Create pharmacy
        Pharmacy pharmacy = pharmacyMapper.toEntity(pharmacyDto, creator);
        pharmacy.setCreatedDate(LocalDate.now());
        pharmacy.setCreatedBy(creator);

        // 3️⃣ Fetch ALL super admins
        List<User> superAdmins = userRepository.findAllSuperAdmins();

        // 4️⃣ Associate pharmacy with ALL super admins
        for (User admin : superAdmins) {
            pharmacy.getMembers().add(admin);
            admin.getPharmacies().add(pharmacy);
        }

        // 5️⃣ Save pharmacy
        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);

        return pharmacyMapper.toDto(savedPharmacy);
    }

//    @Transactional
//    @Override
//    public PharmacyDto savePharmacy(PharmacyDto pharmacyDto, User user) {
//        User persistentUser = userRepository.findById(user.getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Pharmacy pharmacy = pharmacyMapper.toEntity(pharmacyDto, persistentUser);
//        pharmacy.setCreatedDate(LocalDate.now());
//
//        pharmacy.getMembers().add(persistentUser);
//
//        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);
//
//        return pharmacyMapper.toDto(savedPharmacy);
//    }

    @Transactional(readOnly = true)
    @Override
    public List<PharmacyDto> getPharmaciesCreatedByUser(User user) {

        User persistentUser = userRepository.findByIdWithPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return persistentUser.getPharmacies().stream()
                .map(pharmacyMapper::toDto)
                .collect(Collectors.toList());
    }
}

