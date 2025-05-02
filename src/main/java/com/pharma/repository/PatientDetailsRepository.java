package com.pharma.repository;

import com.pharma.entity.DoctorEntity;
import com.pharma.entity.PatientDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientDetailsRepository extends JpaRepository<PatientDetailsEntity, UUID>  {

    List<PatientDetailsEntity> findAllByCreatedBy(Long createdBy);
    Optional<PatientDetailsEntity> findByPatientIdAndCreatedBy(UUID patientId, Long createdBy);
}
