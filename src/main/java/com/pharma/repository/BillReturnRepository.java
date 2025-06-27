package com.pharma.repository;

import com.pharma.entity.BillReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillReturnRepository extends JpaRepository<BillReturnEntity, UUID> {

    List<BillReturnEntity> findAllByCreatedBy(Long createdBy);

    Optional<BillReturnEntity> findByBillReturnIdAndCreatedBy(UUID billReturnId, Long createdBy);

    @Query("SELECT p FROM BillReturnEntity p WHERE p.billReturnId1 LIKE CONCAT('BILLRTN-', :year, '-%') ORDER BY p.billReturnId1 DESC LIMIT 1")
    Optional<BillReturnEntity> findLatestBillReturnForYear(@Param("year") String year);

}
