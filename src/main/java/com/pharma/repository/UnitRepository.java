package com.pharma.repository;

import com.pharma.entity.DoctorEntity;
import com.pharma.entity.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, UUID> {

    List<UnitEntity> findAllByCreatedBy(Long createdBy);

    Optional<UnitEntity> findByUnitIdAndCreatedBy(UUID unitId, Long createdBy);

}
