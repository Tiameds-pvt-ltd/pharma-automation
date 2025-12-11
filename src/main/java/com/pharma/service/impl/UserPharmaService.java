package com.pharma.service.impl;

import com.pharma.entity.Pharmacy;
import com.pharma.entity.User;
import com.pharma.repository.PharmacyRepository;
import com.pharma.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPharmaService {

    private final UserRepository userRepository;
    private final PharmacyRepository pharmacyRepository;

    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existsPharmaByName(String name) {
        return pharmacyRepository.existsByName(name);
    }

    @Transactional(readOnly = true)
    public Set<Pharmacy> getPharmaciesForUser(Long userId) {
        return pharmacyRepository.findPharmaciesByMemberId(userId);
    }
}



//package com.pharma.service.impl;
//
//import com.pharma.entity.User;
//import com.pharma.repository.PharmacyRepository;
//import com.pharma.repository.auth.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//@Transactional
//public class UserPharmaService {
//
//    private final UserRepository userRepository;
//    private final PharmacyRepository pharmacyRepository;
//
//    @Autowired
//    public UserPharmaService(UserRepository userRepository, PharmacyRepository pharmacyRepository) {
//        this.userRepository = userRepository;
//        this.pharmacyRepository =pharmacyRepository;
//    }
//
//    public User getUserById(Long userId) {
//        return userRepository.findById(userId).orElse(null);
//    }
//
//    public boolean existsPharmaByName(String name) {
//        return pharmacyRepository.existsByName(name);
//    }
//}

