package com.pharma.repository;

import com.pharma.entity.SupplierEntity;
import com.pharma.entity.VariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VariantRepository extends JpaRepository<VariantEntity, UUID> {

    List<VariantEntity> findAllByCreatedBy(Long createdBy);

    Optional<VariantEntity> findByVariantIdAndCreatedBy(UUID variantId, Long createdBy);

    List<VariantEntity> findAllByPharmacyId(Long pharmacyId);

    Optional<VariantEntity> findByVariantIdAndPharmacyId(UUID variantId, Long pharmacyId);
}
