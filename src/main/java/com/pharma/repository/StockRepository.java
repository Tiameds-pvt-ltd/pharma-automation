package com.pharma.repository;


import com.pharma.dto.StockDto;
import com.pharma.dto.StockSummaryDto;
import com.pharma.entity.PurchaseOrderEntity;
import com.pharma.entity.StockEntity;
import com.pharma.entity.StockItemEntity;
import com.pharma.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, UUID> {

    List<StockEntity> findAllByCreatedBy(Long createdBy);

    List<StockEntity> findByPurchaseBillNo(String purchaseBillNo);

    Optional<StockEntity> findByInvIdAndCreatedBy(UUID invId, Long createdBy);

    @Query("SELECT p.purchaseBillNo FROM StockEntity p " +
            "WHERE p.supplierId = :supplierId " +
            "AND p.pharmacyId = :pharmacyId " +
            "AND EXTRACT(YEAR FROM p.purchaseDate) = :year " +
            "AND LOWER(p.purchaseBillNo) = LOWER(:purchaseBillNo)")
    List<String> findBillNoBySupplierIdYearAndPharmacy(
            @Param("supplierId") UUID supplierId,
            @Param("pharmacyId") Long pharmacyId,
            @Param("year") int year,
            @Param("purchaseBillNo") String purchaseBillNo
    );

    @Query(value = """
    SELECT *
    FROM pharma_stock_purchase
    WHERE pharmacy_id = :pharmacyId
      AND grn_no LIKE CONCAT('GRN-', :year, '-%')
    ORDER BY CAST(SPLIT_PART(grn_no, '-', 3) AS INTEGER) DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<StockEntity> findLatestGrnNoForYearAndPharmacy(
            @Param("pharmacyId") Long pharmacyId,
            @Param("year") String year
    );

    @Query("SELECT new com.pharma.dto.StockSummaryDto( " +
            "s.invId, s.supplierId, s.pharmacyId, s.purchaseBillNo, s.purchaseDate, " +
            "s.creditPeriod, s.paymentDueDate, s.invoiceAmount, s.totalAmount, " +
            "s.totalCgst, s.totalSgst, s.totalDiscountPercentage, s.totalDiscountAmount, " +
            "s.grandTotal, s.paymentStatus, s.goodStatus, s.grnNo, " +
            "s.createdBy, s.createdDate, s.modifiedBy, s.modifiedDate) " +
            "FROM StockEntity s " +
            "WHERE s.paymentStatus = :paymentStatus " +
            "AND s.supplierId = :supplierId " +
            "AND s.pharmacyId = :pharmacyId")
    List<StockSummaryDto> findStockSummariesByPaymentStatusAndSupplierIdAndPharmacyId(
            @Param("paymentStatus") String paymentStatus,
            @Param("supplierId") UUID supplierId,
            @Param("pharmacyId") Long pharmacyId
    );


    List<StockEntity> findAllByPharmacyId(Long pharmacyId);

    Optional<StockEntity> findByInvIdAndPharmacyId(UUID invId, Long pharmacyId);


}
