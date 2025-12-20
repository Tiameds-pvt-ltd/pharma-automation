package com.pharma.repository;

import com.pharma.entity.BillPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillPaymentRepository extends JpaRepository<BillPaymentEntity, UUID> {
    @Query(value = """
        SELECT *
        FROM pharma_bill_payment
        WHERE pharmacy_id = :pharmacyId
          AND payment_id LIKE CONCAT('PAY-', :yearPart, '-%')
        ORDER BY payment_id DESC
        LIMIT 1
        FOR UPDATE
        """, nativeQuery = true)
    Optional<BillPaymentEntity> findLatestPaymentForYearAndPharmacyForUpdate(
            @Param("pharmacyId") Long pharmacyId,
            @Param("yearPart") String yearPart
    );
}
