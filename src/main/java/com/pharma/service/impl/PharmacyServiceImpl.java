package com.pharma.service.impl;

import com.pharma.dto.PharmacyDto;
import com.pharma.entity.*;
import com.pharma.mapper.PharmacyMapper;
import com.pharma.repository.PharmacyRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.PharmacyService;
import com.pharma.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        pharmacyDto.setCreatedBy(user.getId());
        pharmacyDto.setModifiedBy(user.getId());

        Pharmacy pharmacy = pharmacyMapper.toEntity(pharmacyDto);
        pharmacy.getMembers().add(user);

        Pharmacy savedPharmacy = pharmacyRepository.save(pharmacy);

        return pharmacyMapper.toDto(savedPharmacy);
    }

    @Transactional
    @Override
    public List<PharmacyDto> getPharmaciesCreatedByUser(User user) {
        List<Pharmacy> pharmacies = pharmacyRepository.findAllByCreatedBy(user.getId());
        return pharmacies.stream()
                .map(pharmacyMapper::toDto)
                .collect(Collectors.toList());
    }

}
