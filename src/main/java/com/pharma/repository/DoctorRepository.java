package com.pharma.repository;

import com.pharma.entity.DoctorEntity;
import com.pharma.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, UUID> {

    List<DoctorEntity> findAllByCreatedBy(Long createdBy);

    Optional<DoctorEntity> findByDoctorIdAndCreatedBy(UUID doctorId, Long createdBy);

    boolean existsByDoctorNameAndDoctorMobileAndPharmacyId(String doctorName, Long doctorMobile, Long pharmacyId);

    List<DoctorEntity> findAllByPharmacyId(Long pharmacyId);

    Optional<DoctorEntity> findByDoctorIdAndPharmacyId(UUID doctorId, Long pharmacyId);
}
