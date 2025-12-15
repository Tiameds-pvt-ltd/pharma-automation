package com.pharma.repository;

import com.pharma.entity.PurchaseReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturnEntity, UUID> {

    List<PurchaseReturnEntity> findAllByCreatedBy(Long createdBy);

    Optional<PurchaseReturnEntity> findByReturnIdAndCreatedBy(UUID returnId, Long createdBy);
    @Query(value = """
    SELECT *
    FROM pharma_purchase_return
    WHERE pharmacy_id = :pharmacyId
      AND return_id1 LIKE CONCAT('RTN-', :year, '-%')
    ORDER BY CAST(SPLIT_PART(return_id1, '-', 3) AS INTEGER) DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<PurchaseReturnEntity> findLatestReturnForYearAndPharmacy(
            @Param("pharmacyId") Long pharmacyId,
            @Param("year") String year
    );

    @Query("SELECT COALESCE(SUM(p.returnAmount), 0) FROM PurchaseReturnEntity p " +
            "WHERE p.creditNote = FALSE " +
            "AND p.supplierId = :supplierId " +
            "AND p.pharmacyId = :pharmacyId")
    BigDecimal sumReturnAmountBySupplierIdAndPharmacyId(
            @Param("supplierId") UUID supplierId,
            @Param("pharmacyId") Long pharmacyId);


    @Modifying
    @Query("UPDATE PurchaseReturnEntity pr SET pr.creditNote = true WHERE pr.supplierId = :supplierId")
    int markCreditNoteTrueForSupplier(@Param("supplierId") UUID supplierId);

    List<PurchaseReturnEntity> findAllByPharmacyId(Long pharmacyId);

    Optional<PurchaseReturnEntity> findByReturnIdAndPharmacyId(UUID returnId, Long pharmacyId);
}
