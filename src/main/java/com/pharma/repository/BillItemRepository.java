package com.pharma.repository;

import com.pharma.dto.GstSlabNetPayableDto;
import com.pharma.dto.ItemProfitRowDto;
import com.pharma.entity.BillItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BillItemRepository extends JpaRepository<BillItemEntity, UUID> {

    @Query("""
    SELECT new com.pharma.dto.ItemProfitRowDto(
        bi.batchNo,
        b.billDateTime,
        b.billId1,
        bi.packageQuantity,
        (bi.salePrice * bi.packageQuantity),
        (si.purchasePricePerUnit * bi.packageQuantity),
        ((bi.salePrice * bi.packageQuantity) - (si.purchasePricePerUnit * bi.packageQuantity))
    )
    FROM BillItemEntity bi
    JOIN bi.billEntity b
    LEFT JOIN StockItemEntity si
        ON si.itemId = bi.itemId
       AND si.batchNo = bi.batchNo
    WHERE bi.itemId = :itemId
      AND b.pharmacyId = :pharmacyId
      AND b.billDateTime >= :startDate
      AND b.billDateTime < :endDate
""")
    List<ItemProfitRowDto> findItemProfitDetailsByMonth(
            @Param("itemId") UUID itemId,
            @Param("pharmacyId") Long pharmacyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


        @Query(value = """
        SELECT
            COALESCE(p.gst_percentage, s.gst_percentage) AS gstPercentage,

            COALESCE(s.output_gst_amount, 0) AS outputGstAmount,
            COALESCE(p.input_gst_amount, 0)  AS inputGstAmount,

            (COALESCE(s.output_gst_amount, 0)
             - COALESCE(p.input_gst_amount, 0)) AS netGstPayable

        FROM (
            SELECT
                gst_percentage,
                ROUND(SUM(gst_amount)) AS input_gst_amount
            FROM pharma_stock_purchase_item
            WHERE pharmacy_id = :pharmacyId
              AND created_date >= :startDate
              AND created_date <  :endDate
            GROUP BY gst_percentage
        ) p
        FULL OUTER JOIN (
            SELECT
                gst_percentage,
                ROUND(SUM(gst_amount)) AS output_gst_amount
            FROM pharma_billing_item
            WHERE pharmacy_id = :pharmacyId
              AND created_date >= :startDate
              AND created_date <  :endDate
            GROUP BY gst_percentage
        ) s
        ON p.gst_percentage = s.gst_percentage
        ORDER BY gstPercentage
        """, nativeQuery = true)
        List<GstSlabNetPayableDto> findNetGstSlabWise(
                @Param("pharmacyId") Long pharmacyId,
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate
        );


}
