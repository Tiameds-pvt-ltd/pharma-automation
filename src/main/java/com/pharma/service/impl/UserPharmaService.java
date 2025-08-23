package com.pharma.service.impl;

import com.pharma.entity.User;
import com.pharma.repository.PharmacyRepository;
import com.pharma.repository.auth.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class UserPharmaService {

    private final UserRepository userRepository;
    private final PharmacyRepository pharmacyRepository;

    @Autowired
    public UserPharmaService(UserRepository userRepository, PharmacyRepository pharmacyRepository) {
        this.userRepository = userRepository;
        this.pharmacyRepository =pharmacyRepository;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean existsPharmaByName(String name) {
        return pharmacyRepository.existsByName(name);
    }
}
