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
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SupplierEntity supplierEntity = supplierMapper.mapToEntity(supplierDto);
        supplierEntity.setSupplierId(UUID.randomUUID());
        supplierEntity.setCreatedBy(user.getId());
        supplierEntity.setCreatedDate(LocalDate.now());

        SupplierEntity savedSupplier = supplierRepository.save(supplierEntity);
        return supplierMapper.mapToDto(savedSupplier);
    }

    @Override
    @Transactional
    public List<SupplierDto> getAllSupplier(Long createdById) {
        List<SupplierEntity> suppliers = supplierRepository.findAllByCreatedBy(createdById);
        return suppliers.stream()
                .map(supplierMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupplierDto getSupplierById(Long createdById, UUID supplierId) {
        Optional<SupplierEntity> supplierEntity = supplierRepository.findBySupplierIdAndCreatedBy(supplierId, createdById);

        if (supplierEntity.isEmpty()) {
            throw new ResourceNotFoundException("Supplier not found with ID: " + supplierId + " for user ID: " + createdById);
        }
        return supplierMapper.mapToDto(supplierEntity.get());
    }

    @Override
    @Transactional
    public SupplierDto updateSupplier(Long modifiedById, UUID supplierId, SupplierDto updatedSupplier) {
        Optional<SupplierEntity> supplierEntityOptional = supplierRepository.findById(supplierId);

        if (supplierEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Supplier not found with ID: " + supplierId);
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
        supplierEntity.setSupplierZip(updatedSupplier.getSupplierZip());
        supplierEntity.setSupplierCity(updatedSupplier.getSupplierCity());
        supplierEntity.setSupplierState(updatedSupplier.getSupplierState());
        supplierEntity.setSupplierStatus(updatedSupplier.getSupplierStatus());

        supplierEntity.setModifiedBy(modifiedById);
        supplierEntity.setModifiedDate(LocalDate.now());

        SupplierEntity updatedSuppliers = supplierRepository.save(supplierEntity);
        return supplierMapper.mapToDto(updatedSuppliers);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long createdById, UUID supplierId) {
        Optional<SupplierEntity> supplierEntity = supplierRepository.findBySupplierIdAndCreatedBy(supplierId, createdById);

        if (supplierEntity.isEmpty()) {
            throw new ResourceNotFoundException("Supplier not found with ID: " + supplierId + " for user ID: " + createdById);
        }

        supplierRepository.delete(supplierEntity.get());
    }
}


