package com.pharma.repository;

import com.pharma.dto.BillingGstSummaryDto;
import com.pharma.dto.BillingSummaryDto;
import com.pharma.dto.PaymentSummaryDto;
import com.pharma.entity.BillEntity;
import com.pharma.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, UUID> {

    List<BillEntity> findAllByCreatedBy(Long createdBy);

    Optional<BillEntity> findByBillIdAndCreatedBy(UUID billId, Long createdBy);
    @Query(value = """
    SELECT *
    FROM pharma_billing
    WHERE pharmacy_id = :pharmacyId
      AND bill_id1 LIKE CONCAT('BILL-', :year, '-%')
    ORDER BY CAST(SPLIT_PART(bill_id1, '-', 3) AS INTEGER) DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<BillEntity> findLatestBillForYearAndPharmacy(
            @Param("pharmacyId") Long pharmacyId,
            @Param("year") String year
    );


    @Query(value = "SELECT (p.package_quantity - COALESCE(SUM(b.package_quantity), 0)) AS package_quantity " +
            "FROM pharma_stock_purchase_item p " +
            "LEFT JOIN pharma_billing_item b " +
            "   ON p.item_id = b.item_id " +
            "   AND p.batch_no = b.batch_no " +
            "   AND b.pharmacy_id = :pharmacyId " +
            "WHERE p.item_id = :itemId " +
                    "  AND p.batch_no = :batchNo " +
                    "  AND p.pharmacy_id = :pharmacyId " +
            "GROUP BY p.package_quantity",
            nativeQuery = true)
    Object getPackageQuantityRaw(@Param("itemId") UUID itemId,
                                 @Param("batchNo") String batchNo,
                                 @Param("pharmacyId") Long pharmacyId);

    @Query(value = """
SELECT 
  COALESCE(r.total_return_amount, 0) AS total_return_amount,
  r.total_return_bills,
  b.paid_total_amount,
  b.paid_total_bills,
  b.unpaid_total_amount,
  b.unpaid_total_bills
FROM
  (
    SELECT 
      COALESCE(SUM(grand_total), 0) AS total_return_amount,
      COUNT(*) AS total_return_bills
    FROM pharma_billing_return
    WHERE DATE(bill_return_date_time) = :selectedDate
      AND pharmacy_id = :pharmacyId
  ) AS r
CROSS JOIN
  (
    SELECT 
      SUM(CASE WHEN payment_status = 'Paid' THEN grand_total ELSE 0 END) AS paid_total_amount,
      COUNT(CASE WHEN payment_status = 'Paid' THEN 1 ELSE NULL END) AS paid_total_bills,
      SUM(CASE WHEN payment_status = 'Pending' THEN grand_total ELSE 0 END) AS unpaid_total_amount,
      COUNT(CASE WHEN payment_status = 'Pending' THEN 1 ELSE NULL END) AS unpaid_total_bills
    FROM pharma_billing
    WHERE DATE(bill_date_time) = :selectedDate
      AND pharmacy_id = :pharmacyId
  ) AS b
""", nativeQuery = true)
    BillingSummaryDto getBillingSummaryByDateAndPharmacy(
            @Param("selectedDate") LocalDate selectedDate,
            @Param("pharmacyId") Long pharmacyId
    );


    @Query(value = """
SELECT 
  COALESCE(SUM(CASE 
    WHEN payment_type IN ('creditCard', 'debitCard') THEN grand_total 
    ELSE 0 
  END), 0) AS card_total,

  COALESCE(COUNT(CASE 
    WHEN payment_type IN ('creditCard', 'debitCard') THEN 1 
    ELSE NULL 
  END), 0) AS card_count,

  COALESCE(SUM(CASE 
    WHEN payment_type IN ('upi', 'net_banking') THEN grand_total 
    ELSE 0 
  END), 0) AS upi_net_total,

  COALESCE(COUNT(CASE 
    WHEN payment_type IN ('upi', 'net_banking') THEN 1 
    ELSE NULL 
  END), 0) AS upi_net_count,

  COALESCE(SUM(CASE 
    WHEN payment_type = 'cash' THEN grand_total 
    ELSE 0 
  END), 0) AS cash_total,

  COALESCE(COUNT(CASE 
    WHEN payment_type = 'cash' THEN 1 
    ELSE NULL 
  END), 0) AS cash_count,

  COALESCE(SUM(CASE 
    WHEN payment_type = 'upiCash' THEN grand_total 
    ELSE 0 
  END), 0) AS upi_cash_total,

  COALESCE(COUNT(CASE 
    WHEN payment_type = 'upiCash' THEN 1 
    ELSE NULL 
  END), 0) AS upi_cash_count

FROM pharma_billing
WHERE DATE(bill_date_time) = :selectedDate
  AND pharmacy_id = :pharmacyId
""", nativeQuery = true)
    PaymentSummaryDto getPaymentSummaryByDateAndPharmacy(
            @Param("selectedDate") LocalDate selectedDate,
            @Param("pharmacyId") Long pharmacyId
    );

    @Query(value = """
    SELECT 
        bill_id1 AS billId1, 
        DATE(bill_date_time) AS billDate, 
        sub_total AS subTotal, 
        total_gst AS totalGst, 
        grand_total AS grandTotal
    FROM pharma_billing
    WHERE pharmacy_id = :pharmacyId
      AND DATE(bill_date_time) = :inputDate
    """, nativeQuery = true)
    List<BillingGstSummaryDto> getBillingGstSummaryByDate(
            @Param("pharmacyId") Long pharmacyId,
            @Param("inputDate") LocalDate inputDate
    );

    @Query(value = """
    SELECT 
        bill_id1 AS billId1, 
        DATE(bill_date_time) AS billDate, 
        sub_total AS subTotal, 
        total_gst AS totalGst, 
        grand_total AS grandTotal
    FROM pharma_billing
    WHERE pharmacy_id = :pharmacyId
      AND TO_CHAR(bill_date_time, 'YYYY-MM') = :inputMonth
    """, nativeQuery = true)
    List<BillingGstSummaryDto> getBillingGstSummaryByMonth(
            @Param("pharmacyId") Long pharmacyId,
            @Param("inputMonth") String inputMonth
    );

    List<BillEntity> findAllByPharmacyId(Long pharmacyId);

    Optional<BillEntity> findByBillIdAndPharmacyId(UUID billId, Long pharmacyId);

}



