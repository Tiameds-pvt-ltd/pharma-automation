package com.pharma.repository;

import com.pharma.entity.PatientDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientDetailsRepository extends JpaRepository<PatientDetailsEntity, Long>  {
}
