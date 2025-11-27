package com.pharma.repository;

import com.pharma.entity.PatientDetailsEntity;
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

    @Query("SELECT p FROM PatientDetailsEntity p WHERE p.patientId1 LIKE CONCAT('PAT-', :year, '-%') ORDER BY p.patientId1 DESC LIMIT 1")
    Optional<PatientDetailsEntity> findLatestPatientForYear(@Param("year") String year);

    boolean existsByPatientNameAndPhone(String patientName, Long phone);

}
