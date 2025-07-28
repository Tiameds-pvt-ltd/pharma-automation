package com.pharma.repository;

import com.pharma.entity.Pharmacy;
import com.pharma.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

//  boolean existsByNameAndUser(String name, User user);

    List<Pharmacy> findAllByCreatedBy(Long createdById);

    Optional<Pharmacy> findByPharmacyIdAndCreatedBy(Long pharmacyId, Long createdById);

    Optional<Pharmacy> findById(Long pharmacyId);

    boolean existsByName(String name);
}
