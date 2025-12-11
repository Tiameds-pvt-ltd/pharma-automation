package com.pharma.repository;

import com.pharma.entity.Pharmacy;
import com.pharma.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    List<Pharmacy> findAllByCreatedBy(User createdBy);

    Optional<Pharmacy> findById(Long pharmacyId);

    boolean existsByName(String name);

    @Query("""
        SELECT DISTINCT p
        FROM Pharmacy p
        JOIN FETCH p.members m
        LEFT JOIN FETCH p.createdBy cb
        WHERE m.id = :userId
    """)
    Set<Pharmacy> findPharmaciesByMemberId(@Param("userId") Long userId);

    @Query("""
        SELECT DISTINCT p
        FROM Pharmacy p
        JOIN FETCH p.members m
        LEFT JOIN FETCH p.createdBy cb
        WHERE p.pharmacyId = :pharmacyId
        AND m.id = :userId
    """)
    Optional<Pharmacy> findPharmacyForUser(@Param("pharmacyId") Long pharmacyId,
                                           @Param("userId") Long userId);
}


//package com.pharma.repository;
//
//import com.pharma.entity.Pharmacy;
//import com.pharma.entity.User;
//import jakarta.transaction.Transactional;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//
//public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
//
////  boolean existsByNameAndUser(String name, User user);
//
//    List<Pharmacy> findAllByCreatedBy(User createdById);
//
////    Optional<Pharmacy> findByPharmacyIdAndCreatedBy(Long pharmacyId, Long createdById);
//
//    Optional<Pharmacy> findById(Long pharmacyId);
//
//    boolean existsByName(String name);
//
//    @Transactional
//    @Query("SELECT l FROM Pharmacy l JOIN l.members m WHERE m.id = :userId")
//    Set<Pharmacy> findPharmacyByUserId(@Param("userId") Long userId);
//
//
//    @Query("SELECT p FROM Pharmacy p JOIN p.members m WHERE p.pharmacyId = :pharmacyId AND m.id = :userId")
//    Optional<Pharmacy> findPharmacyForUser(@Param("pharmacyId") Long pharmacyId, @Param("userId") Long userId);
//
//}
