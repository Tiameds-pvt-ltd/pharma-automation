package com.pharma.repository;

import com.pharma.entity.PurchaseOrderEntity;
import com.pharma.entity.PurchaseReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturnEntity, UUID> {

    List<PurchaseReturnEntity> findAllByCreatedBy(Long createdBy);

    Optional<PurchaseReturnEntity> findByReturnIdAndCreatedBy(UUID returnId, Long createdBy);

    @Query("SELECT r FROM PurchaseReturnEntity r WHERE r.returnId1 LIKE CONCAT('RTN-', :date, '-%') ORDER BY r.returnId1 DESC LIMIT 1")
    Optional<PurchaseReturnEntity> findLatestReturnForToday(@Param("date") String date);
}
