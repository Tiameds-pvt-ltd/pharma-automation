package com.pharma.repository;

import com.pharma.dto.*;
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


    @Query(
            value = """
        SELECT
            i.item_name                    AS itemName,
            SUM(t.quantity_sold)           AS totalQuantitySold,
            SUM(t.sales_price)             AS totalSalesPrice,
            SUM(t.cost_price)              AS totalCostPrice,
            SUM(t.sales_price) - SUM(t.cost_price) AS profit
        FROM (
            SELECT
                bi.item_id,
                bi.batch_no,
                SUM(bi.package_quantity) AS quantity_sold,
                ROUND(SUM(bi.net_total)) AS sales_price,
                ROUND(
                    SUM(bi.package_quantity * sp.purchase_price_per_unit)
                ) AS cost_price
            FROM pharma_billing_item bi
            JOIN pharma_billing b
                ON b.bill_id = bi.bill_id
            LEFT JOIN pharma_stock_purchase_item sp
                ON sp.item_id = bi.item_id
               AND sp.batch_no = bi.batch_no
            WHERE b.doctor_id   = :doctorId
              AND b.pharmacy_id = :pharmacyId
              AND b.bill_date_time >= :startDate
              AND b.bill_date_time <  :endDate
            GROUP BY bi.item_id, bi.batch_no
        ) t
        JOIN pharma_item i
            ON i.item_id = t.item_id
        GROUP BY i.item_id, i.item_name
        ORDER BY i.item_name
        """,
            nativeQuery = true
    )
    List<ItemProfitByDoctorDto> findDoctorWiseItemProfit(
            @Param("doctorId") UUID doctorId,
            @Param("pharmacyId") Long pharmacyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query(
            value = """
            SELECT
                DATE(b.bill_date_time) AS bill_date,

                SUM(bi.package_quantity) AS total_quantity_sold,

                ROUND(SUM(bi.gross_total)::numeric) AS sales_price,

                ROUND(
                    SUM(
                        bi.package_quantity
                        * COALESCE(sp.purchase_price_per_unit, 0)
                        * (1 + COALESCE(sp.gst_percentage, 0) / 100)
                    )::numeric
                ) AS cost_price,

                ROUND(
                    ROUND(SUM(bi.gross_total)::numeric)
                    -
                    ROUND(
                        SUM(
                            bi.package_quantity
                            * COALESCE(sp.purchase_price_per_unit, 0)
                            * (1 + COALESCE(sp.gst_percentage, 0) / 100)
                        )::numeric
                    )
                ) AS profit

            FROM pharma_billing_item bi
            JOIN pharma_billing b
                ON b.bill_id = bi.bill_id

            LEFT JOIN pharma_stock_purchase_item sp
                ON sp.item_id  = bi.item_id
               AND sp.batch_no = bi.batch_no
               AND sp.pharmacy_id = :pharmacyId   

            WHERE b.pharmacy_id = :pharmacyId     
              AND b.bill_date_time >= :startDate
              AND b.bill_date_time <  :endDate

            GROUP BY DATE(b.bill_date_time)
            ORDER BY bill_date
        """,
            nativeQuery = true
    )
    List<DailySalesCostProfitDto> findDailySalesCostProfitPharmacyWise(
            @Param("pharmacyId") Long pharmacyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query(value = """
    SELECT
        item_name        AS itemName,
        hsn_no           AS hsnNo,
        MAX(gst_percentage) AS gstPercentage,

        SUM(quantity) AS quantity,

        ROUND(SUM(gross_sale_price), 2) AS grossSalePrice,
        ROUND(SUM(sale_gst_amount), 2)  AS saleGstAmount,
        ROUND(SUM(net_sale_price), 2)   AS netSalePrice,

        ROUND(SUM(gross_cost_price), 2) AS grossCostPrice,
        ROUND(SUM(cost_gst_amount), 2)  AS costGstAmount,
        ROUND(SUM(net_cost_price), 2)   AS netCostPrice,

        ROUND(SUM(gross_profit), 2)     AS grossProfit,
        ROUND(SUM(net_profit), 2)       AS netProfit

    FROM (
        SELECT
            bi.item_id,
            i.item_name,
            i.hsn_no,
            bi.batch_no,
            bi.gst_percentage,

            SUM(bi.package_quantity) AS quantity,

            ROUND(
                SUM(bi.package_quantity)::numeric * bi.sale_price,
                2
            ) AS gross_sale_price,

            ROUND(
                (SUM(bi.package_quantity)::numeric * bi.sale_price)
                * (bi.gst_percentage / 100.0),
                2
            ) AS sale_gst_amount,

            ROUND(
                (SUM(bi.package_quantity)::numeric * bi.sale_price)
                * (1 + bi.gst_percentage / 100.0),
                2
            ) AS net_sale_price,

            ROUND(
                SUM(bi.package_quantity)::numeric * si.purchase_price_per_unit,
                2
            ) AS gross_cost_price,

            ROUND(
                (SUM(bi.package_quantity)::numeric * si.purchase_price_per_unit)
                * (bi.gst_percentage / 100.0),
                2
            ) AS cost_gst_amount,

            ROUND(
                (SUM(bi.package_quantity)::numeric * si.purchase_price_per_unit)
                * (1 + bi.gst_percentage / 100.0),
                2
            ) AS net_cost_price,

            ROUND(
                (SUM(bi.package_quantity)::numeric * bi.sale_price)
                -
                (SUM(bi.package_quantity)::numeric * si.purchase_price_per_unit),
                2
            ) AS gross_profit,

            ROUND(
                (
                    (SUM(bi.package_quantity)::numeric * bi.sale_price)
                    * (1 + bi.gst_percentage / 100.0)
                )
                -
                (
                    (SUM(bi.package_quantity)::numeric * si.purchase_price_per_unit)
                    * (1 + bi.gst_percentage / 100.0)
                ),
                2
            ) AS net_profit

        FROM pharma_billing_item bi
        JOIN pharma_billing b
            ON b.bill_id = bi.bill_id
        JOIN pharma_item i
            ON i.item_id = bi.item_id
        LEFT JOIN pharma_stock_purchase_item si
            ON si.item_id = bi.item_id
           AND si.batch_no = bi.batch_no

        WHERE
            b.pharmacy_id = :pharmacyId
            AND b.bill_date_time >= :startDate
            AND b.bill_date_time <  :endDate

        GROUP BY
            bi.item_id,
            i.item_name,
            i.hsn_no,
            bi.batch_no,
            bi.sale_price,
            bi.gst_percentage,
            si.purchase_price_per_unit
    ) batch_level

    GROUP BY
        item_name,
        hsn_no

    ORDER BY
        item_name
    """, nativeQuery = true)
    List<ItemProfitSummaryDto> findItemProfitSummary(
            @Param("pharmacyId") Long pharmacyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}
