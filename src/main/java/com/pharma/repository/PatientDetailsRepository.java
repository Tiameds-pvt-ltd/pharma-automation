package com.pharma.repository;

import com.pharma.entity.PatientDetailsEntity;
import com.pharma.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientDetailsRepository extends JpaRepository<PatientDetailsEntity, UUID>  {

    List<PatientDetailsEntity> findAllByCreatedBy(Long createdBy);

    Optional<PatientDetailsEntity> findByPatientIdAndCreatedBy(UUID patientId, Long createdBy);

    @Query(value = """
    SELECT *
    FROM pharma_patient_details
    WHERE pharmacy_id = :pharmacyId
      AND patient_id1 LIKE CONCAT('PAT-', :year, '-%')
    ORDER BY CAST(SPLIT_PART(patient_id1, '-', 3) AS INTEGER) DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<PatientDetailsEntity> findLatestPatientForYearAndPharmacy(
            @Param("pharmacyId") Long pharmacyId,
            @Param("year") String year
    );


    boolean existsByFirstNameAndPhoneAndPharmacyId(String firstName, Long phone, Long pharmacyId);

    @Query("SELECT MAX(p.patientId1) FROM PatientDetailsEntity p")
    String findMaxPatientId1();

    List<PatientDetailsEntity> findAllByPharmacyId(Long pharmacyId);

    Optional<PatientDetailsEntity> findByPatientIdAndPharmacyId(UUID patientId, Long pharmacyId);
}
