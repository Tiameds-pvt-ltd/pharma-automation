package com.pharma.repository;

import com.pharma.entity.Pharmacist;
import com.pharma.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PharmacyRepository extends JpaRepository<Pharmacy, UUID> {

    boolean existsByPharmacyNameAndPharmacists(String pharmacyName, Pharmacist pharmacist);

    List<Pharmacy> findAllByCreatedBy(Long createdById);

    Optional<Pharmacy> findByPharmacyIdAndCreatedBy(UUID pharmacyId, Long createdById);


}
