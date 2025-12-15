package com.pharma.repository;

import com.pharma.entity.PurchaseOrderEntity;
import com.pharma.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, UUID> {

    List<PurchaseOrderEntity> findAllByCreatedBy(Long createdBy);

    Optional<PurchaseOrderEntity> findByOrderIdAndCreatedBy(UUID orderId, Long createdBy);

//    @Query("SELECT p FROM PurchaseOrderEntity p WHERE p.orderId1 LIKE CONCAT('ORD-', :year, '-%') ORDER BY p.orderId1 DESC LIMIT 1")
//    Optional<PurchaseOrderEntity> findLatestOrderForYear(@Param("year") String year);

    @Query(value = """
    SELECT *
    FROM pharma_purchase_order
    WHERE pharmacy_id = :pharmacyId
      AND order_id1 LIKE CONCAT('ORD-', :year, '-%')
    ORDER BY CAST(SPLIT_PART(order_id1, '-', 3) AS INTEGER) DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<PurchaseOrderEntity> findLatestOrderForYearAndPharmacy(
            @Param("pharmacyId") Long pharmacyId,
            @Param("year") String year
    );


    List<PurchaseOrderEntity> findAllByPharmacyId(Long pharmacyId);

    Optional<PurchaseOrderEntity> findByOrderIdAndPharmacyId(UUID orderId, Long pharmacyId);
}
