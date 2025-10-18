package com.pharma.repository;

import com.pharma.entity.PurchaseOrderEntity;
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

    @Query("SELECT p FROM PurchaseReturnEntity p WHERE p.returnId1 LIKE CONCAT('RTN-', :year, '-%') ORDER BY p.returnId1 DESC LIMIT 1")
    Optional<PurchaseReturnEntity> findLatestReturnForYear(@Param("year") String year);

    @Query("SELECT COALESCE(SUM(p.returnAmount), 0) FROM PurchaseReturnEntity p " +
            "WHERE p.creditNote = FALSE " +
            "AND p.supplierId = :supplierId " +
            "AND p.createdBy = :createdBy")
    BigDecimal sumReturnAmountBySupplierIdAndCreditNoteAndCreatedBy(
            @Param("supplierId") UUID supplierId,
            @Param("createdBy") Long createdBy);



    @Modifying
    @Query("UPDATE PurchaseReturnEntity pr SET pr.creditNote = true WHERE pr.supplierId = :supplierId")
    int markCreditNoteTrueForSupplier(@Param("supplierId") UUID supplierId);


}
