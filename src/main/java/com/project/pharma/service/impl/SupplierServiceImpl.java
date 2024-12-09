package com.project.pharma.service.impl;

import com.project.pharma.dto.SupplierDto;
import com.project.pharma.entity.SupplierEntity;
import com.project.pharma.exception.ResourceNotFoundException;
import com.project.pharma.mapper.SupplierMapper;
import com.project.pharma.repository.SupplierRepository;
import com.project.pharma.service.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private SupplierRepository supplierRepository;

    @Override
    public SupplierDto createSupplier(SupplierDto supplierDto) {
        SupplierEntity supplierEntity = SupplierMapper.mapToEntity(supplierDto);
        SupplierEntity saveSupplier = supplierRepository.save(supplierEntity);
        return SupplierMapper.mapToDto(saveSupplier);
    }

    @Override
    public SupplierDto getSupplierById(Integer supplierId) {
        SupplierEntity supplier= supplierRepository.findById(supplierId).
                orElseThrow(() -> new ResourceNotFoundException("Supplier does not exists with given ID :" + supplierId));
        return SupplierMapper.mapToDto(supplier);
    }

    @Override
    public List<SupplierDto> getAllSupplier() {
        List<SupplierEntity> supplierEntities = supplierRepository.findAll();
        return supplierEntities.stream().map((supplierEntity) -> SupplierMapper.mapToDto(supplierEntity)).collect(Collectors.toList());
    }

    @Override
    public SupplierDto updateSupplier(Integer supplierId, SupplierDto updatedSupplier) {
        SupplierEntity supplierEntity = supplierRepository.findById(supplierId).
                orElseThrow(() -> new ResourceNotFoundException("Supplier does not exists with the given ID : " + supplierId));

        supplierEntity.setSupplierName(updatedSupplier.getSupplierName());
        supplierEntity.setSupplierMobile(updatedSupplier.getSupplierMobile());
        supplierEntity.setSupplierEmail(updatedSupplier.getSupplierEmail());
        supplierEntity.setSupplierGstinNo(updatedSupplier.getSupplierGstinNo());
        supplierEntity.setSupplierGstType(updatedSupplier.getSupplierGstType());
        supplierEntity.setSupplierAddress(updatedSupplier.getSupplierAddress());

        SupplierEntity updatedSupplierObj = supplierRepository.save(supplierEntity);
        return SupplierMapper.mapToDto(updatedSupplierObj);
    }

    @Override
    public void deleteSupplier(Integer supplierId) {
        SupplierEntity supplierEntity = supplierRepository.findById(supplierId).
                orElseThrow(() -> new ResourceNotFoundException("Supplier does not exists with the given ID : " + supplierId));

        supplierRepository.deleteById(supplierId);

    }
}


