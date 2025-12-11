package com.pharma.service.impl;

import com.pharma.dto.SupplierDto;
import com.pharma.entity.DoctorEntity;
import com.pharma.entity.SupplierEntity;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.SupplierMapper;

import com.pharma.repository.SupplierRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.SupplierService;


import com.pharma.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional
    public SupplierDto createSupplier(SupplierDto supplierDto, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(supplierDto.getPharmacyId()));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        SupplierEntity supplierEntity = supplierMapper.mapToEntity(supplierDto);
        supplierEntity.setSupplierId(UUID.randomUUID());
        supplierEntity.setCreatedBy(user.getId());
        supplierEntity.setCreatedDate(LocalDate.now());

        supplierEntity.setPharmacyId(supplierDto.getPharmacyId());

        SupplierEntity savedSupplier = supplierRepository.save(supplierEntity);
        return supplierMapper.mapToDto(savedSupplier);
    }

    @Override
    @Transactional
    public List<SupplierDto> getAllSupplier(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<SupplierEntity> suppliers = supplierRepository.findAllByPharmacyId(pharmacyId);
        return suppliers.stream()
                .map(supplierMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupplierDto getSupplierById(Long pharmacyId, UUID supplierId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<SupplierEntity> supplierEntity = supplierRepository.findBySupplierIdAndPharmacyId(supplierId, pharmacyId);

        if (supplierEntity.isEmpty()) {
            throw new ResourceNotFoundException("Supplier not found with ID: " + supplierId + " for pharmacy ID: " + pharmacyId);
        }
        return supplierMapper.mapToDto(supplierEntity.get());
    }

    @Override
    @Transactional
    public SupplierDto updateSupplier(Long pharmacyId, UUID supplierId, SupplierDto updatedSupplier, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<SupplierEntity> supplierEntityOptional =
                supplierRepository.findBySupplierIdAndPharmacyId(supplierId, pharmacyId);

        if (supplierEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Supplier not found with ID: " + supplierId+
                    " for pharmacy ID: " + pharmacyId);
        }

        SupplierEntity supplierEntity = supplierEntityOptional.get();

        supplierEntity.setSupplierName(updatedSupplier.getSupplierName());
        supplierEntity.setContactPerson(updatedSupplier.getContactPerson());
        supplierEntity.setSupplierMobile(updatedSupplier.getSupplierMobile());
        supplierEntity.setSupplierEmail(updatedSupplier.getSupplierEmail());
        supplierEntity.setSupplierGstinNo(updatedSupplier.getSupplierGstinNo());
        supplierEntity.setSupplierGstType(updatedSupplier.getSupplierGstType());
        supplierEntity.setSupplierDlno(updatedSupplier.getSupplierDlno());
        supplierEntity.setSupplierAddress(updatedSupplier.getSupplierAddress());
        supplierEntity.setSupplierStreet(updatedSupplier.getSupplierStreet());
        supplierEntity.setSupplierCity(updatedSupplier.getSupplierCity());
        supplierEntity.setSupplierZip(updatedSupplier.getSupplierZip());
        supplierEntity.setSupplierState(updatedSupplier.getSupplierState());
        supplierEntity.setSupplierStatus(updatedSupplier.getSupplierStatus());

        supplierEntity.setModifiedBy(user.getId());
        supplierEntity.setModifiedDate(LocalDate.now());

        SupplierEntity updatedSuppliers = supplierRepository.save(supplierEntity);
        return supplierMapper.mapToDto(updatedSuppliers);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long pharmacyId, UUID supplierId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<SupplierEntity> supplierEntityOptional =
                supplierRepository.findBySupplierIdAndPharmacyId(supplierId, pharmacyId);

        if (supplierEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Supplier not found with ID: " + supplierId + " for pharmacy ID: " + pharmacyId);
        }

        supplierRepository.delete(supplierEntityOptional.get());
    }
}


