package com.pharma.repository;


import com.pharma.entity.DoctorEntity;
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
}
