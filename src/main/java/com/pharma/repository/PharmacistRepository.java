//package com.pharma.repository;
//
//import com.pharma.entity.Pharmacist;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public interface PharmacistRepository extends JpaRepository<Pharmacist, UUID> {
//
//    Optional<Pharmacist> findByPharmacistName(String pharmacistName);
//
//    List<Pharmacist> findAllByCreatedBy(Long createdById);
//
//    Optional<Pharmacist> findByPharmacistIdAndCreatedBy(UUID pharmacistId, Long createdById);
//
//}
