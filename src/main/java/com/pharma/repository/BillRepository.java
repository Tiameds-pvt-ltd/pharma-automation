package com.pharma.repository;

import com.pharma.dto.BillingGstSummaryDto;
import com.pharma.dto.BillingSummaryDto;
import com.pharma.dto.PaymentSummaryDto;
import com.pharma.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, UUID> {

    List<BillEntity> findAllByCreatedBy(Long createdBy);

    Optional<BillEntity> findByBillIdAndCreatedBy(UUID billId, Long createdBy);

    @Query("SELECT p FROM BillEntity p WHERE p.billId1 LIKE CONCAT('BILL-', :year, '-%') ORDER BY p.billId1 DESC LIMIT 1")
    Optional<BillEntity> findLatestBillForYear(@Param("year") String year);


    @Query(value = "SELECT (p.package_quantity - b.package_quantity) AS package_quantity " +
            "FROM pharma_stock_purchase_item p " +
            "INNER JOIN pharma_billing_item b ON p.item_id = b.item_id AND p.batch_no = b.batch_no " +
            "WHERE p.item_id = :itemId AND p.batch_no = :batchNo", nativeQuery = true)
    Object getPackageQuantityRaw(@Param("itemId") String itemId, @Param("batchNo") String batchNo);

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
          AND created_by = :createdBy
      ) AS r
    CROSS JOIN
      (
        SELECT 
          SUM(CASE WHEN payment_status = 'paid' THEN grand_total ELSE 0 END) AS paid_total_amount,
          COUNT(CASE WHEN payment_status = 'paid' THEN 1 ELSE NULL END) AS paid_total_bills,
          SUM(CASE WHEN payment_status = 'pending' THEN grand_total ELSE 0 END) AS unpaid_total_amount,
          COUNT(CASE WHEN payment_status = 'pending' THEN 1 ELSE NULL END) AS unpaid_total_bills
        FROM pharma_billing
        WHERE DATE(bill_date_time) = :selectedDate
          AND created_by = :createdBy
      ) AS b
    """, nativeQuery = true)
    BillingSummaryDto getBillingSummaryByDateAndCreatedBy(
            @Param("selectedDate") LocalDate selectedDate,
            @Param("createdBy") Long createdBy
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
      AND created_by = :createdBy
    """, nativeQuery = true)
    PaymentSummaryDto getPaymentSummaryByDateAndCreatedBy(
            @Param("selectedDate") LocalDate selectedDate,
            @Param("createdBy") Long createdBy
    );


    @Query(value = """
    SELECT 
        bill_id1 AS billId1, 
        DATE(bill_date_time) AS billDate, 
        sub_total AS subTotal, 
        total_gst AS totalGst, 
        grand_total AS grandTotal
    FROM pharma_billing
    WHERE created_by = :createdBy
      AND DATE(bill_date_time) = :inputDate
    """, nativeQuery = true)
    List<BillingGstSummaryDto> getBillingGstSummaryByDate(
            @Param("createdBy") Long createdBy,
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
    WHERE created_by = :createdBy
      AND TO_CHAR(bill_date_time, 'YYYY-MM') = :inputMonth
    """, nativeQuery = true)
    List<BillingGstSummaryDto> getBillingGstSummaryByMonth(
            @Param("createdBy") Long createdBy,
            @Param("inputMonth") String inputMonth
    );

}



