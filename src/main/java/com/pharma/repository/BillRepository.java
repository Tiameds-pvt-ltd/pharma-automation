package com.pharma.repository;

import com.pharma.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillRepository extends JpaRepository<BillEntity, UUID> {

    List<BillEntity> findAllByCreatedBy(Long createdBy);

    Optional<BillEntity> findByBillIdAndCreatedBy(UUID billId, Long createdBy);

    @Query("SELECT p FROM BillEntity p WHERE p.billId1 LIKE CONCAT('BILL-', :year, '-%') ORDER BY p.billId1 DESC LIMIT 1")
    Optional<BillEntity> findLatestBillForYear(@Param("year") String year);
}
