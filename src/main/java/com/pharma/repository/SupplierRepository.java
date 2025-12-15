package com.pharma.repository;

import com.pharma.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, UUID> {

    List<SupplierEntity> findAllByCreatedBy(Long createdBy);

    Optional<SupplierEntity> findBySupplierIdAndCreatedBy(UUID supplierId, Long createdBy);

    boolean existsBySupplierNameAndPharmacyId(String supplierName, Long pharmacyId);

    boolean existsBySupplierMobileAndPharmacyId(Long supplierMobile, Long pharmacyId);

    boolean existsBySupplierGstinNoAndPharmacyId(String supplierGstinNo, Long pharmacyId);


    List<SupplierEntity> findAllByPharmacyId(Long pharmacyId);

    Optional<SupplierEntity> findBySupplierIdAndPharmacyId(UUID supplierId, Long pharmacyId);


}
