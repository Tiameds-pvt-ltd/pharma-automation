package com.pharma.service.impl;

import com.pharma.entity.Pharmacy;
import com.pharma.entity.User;
import com.pharma.repository.PharmacyRepository;
import com.pharma.repository.auth.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class UserPharmacyService {

    private final UserRepository userRepository;
    private final PharmacyRepository pharmacyRepository;

    public UserPharmacyService(UserRepository userRepository, PharmacyRepository pharmacyRepository) {
        this.userRepository = userRepository;
        this.pharmacyRepository = pharmacyRepository;
    }

    public boolean existsPharmacyByName(String name) {
        return pharmacyRepository.existsByName(name);
    }


    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public Pharmacy getPharmacyWithMembers(Long pharmacyId) {
        return pharmacyRepository.findById(pharmacyId).orElse(null);
    }
}
