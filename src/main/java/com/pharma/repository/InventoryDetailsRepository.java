package com.pharma.repository;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.ExpiredStockView;
import com.pharma.dto.InventoryView;
import com.pharma.entity.InventoryDetailsEntity;
import com.pharma.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryDetailsRepository extends JpaRepository<InventoryDetailsEntity, UUID> {

   Optional<InventoryDetailsEntity> findByItemIdAndBatchNo(UUID itemId, String batchNo);

    Optional<InventoryDetailsEntity> findByItemIdAndBatchNoAndPharmacyId(
            UUID itemId, String batchNo, Long pharmacyId);


    Optional<InventoryDetailsEntity> findByItemIdAndBatchNoAndModifiedBy(UUID itemId, String batchNo, Long modifiedBy);

    List<InventoryDetailsEntity> findAllByPharmacyId(Long pharmacyId);

    List<InventoryDetailsEntity> findAllByCreatedBy(Long createdBy);

//    @Query("""
//    SELECT DISTINCT new com.pharma.dto.ExpiredStockDto(
//        i.itemId,
//        itm.itemName,
//        i.batchNo,
//        i.packageQuantity,
//        i.expiryDate,
//        s.supplierId,
//        sup.supplierName
//    )
//    FROM InventoryDetailsEntity i
//    JOIN StockItemEntity e
//        ON i.itemId = e.itemId AND i.batchNo = e.batchNo
//    JOIN e.stockEntity s
//    JOIN ItemEntity itm ON i.itemId = itm.itemId
//    JOIN SupplierEntity sup ON s.supplierId = sup.supplierId
//    WHERE FUNCTION('date_trunc', 'year', i.expiryDate) = FUNCTION('date_trunc', 'year', CURRENT_DATE)
//      AND i.createdBy = :createdById
//""")
//    List<ExpiredStockDto> findCurrentYearStockWithSupplier(@Param("createdById") Long createdById);

 @Query("""
    SELECT DISTINCT new com.pharma.dto.ExpiredStockDto(
        i.itemId,
        itm.itemName,
        i.batchNo,
        i.packageQuantity,
        i.expiryDate,
        s.supplierId,
        sup.supplierName
    )
    FROM InventoryDetailsEntity i
    JOIN StockItemEntity e 
        ON i.itemId = e.itemId 
       AND i.batchNo = e.batchNo
       AND e.pharmacyId = :pharmacyId
    JOIN e.stockEntity s
    JOIN ItemEntity itm ON i.itemId = itm.itemId
    JOIN SupplierEntity sup ON s.supplierId = sup.supplierId
    WHERE FUNCTION('date_trunc', 'year', i.expiryDate) = FUNCTION('date_trunc', 'year', CURRENT_DATE)
      AND i.pharmacyId = :pharmacyId
""")
 List<ExpiredStockDto> findCurrentYearStockWithSupplier(@Param("pharmacyId") Long pharmacyId);


// @Query(value = """
//    SELECT DISTINCT
//        i.item_id AS itemId,
//        itm.item_name AS itemName,
//        i.batch_no AS batchNo,
//        i.package_quantity AS packageQuantity,
//        i.expiry_date AS expiryDate,
//        s.supplier_id AS supplierId,
//        sup.supplier_name AS supplierName
//    FROM pharma_inventory_details i
//    JOIN pharma_stock_purchase_item e
//        ON i.item_id = e.item_id AND i.batch_no = e.batch_no
//    JOIN pharma_stock_purchase s ON e.inv_id = s.inv_id
//    JOIN pharma_item itm ON i.item_id = itm.item_id
//    JOIN pharma_supplier sup ON s.supplier_id = sup.supplier_id
//    WHERE i.expiry_date >= date_trunc('month', CURRENT_DATE + interval '1 month')
//      AND i.expiry_date < date_trunc('month', CURRENT_DATE + interval '4 month')
//      AND i.created_by = :createdById
//""", nativeQuery = true)
//    List<ExpiredStockView> findNextThreeMonthsStockWithSupplier(@Param("createdById") Long createdById);

 @Query(value = """
    SELECT DISTINCT 
        i.item_id AS itemId,
        itm.item_name AS itemName,
        i.batch_no AS batchNo,
        i.package_quantity AS packageQuantity,
        i.expiry_date AS expiryDate,
        s.supplier_id AS supplierId,
        sup.supplier_name AS supplierName
    FROM pharma_inventory_details i
    JOIN pharma_stock_purchase_item e 
        ON i.item_id = e.item_id 
       AND i.batch_no = e.batch_no
       AND e.pharmacy_id = :pharmacyId
    JOIN pharma_stock_purchase s 
        ON e.inv_id = s.inv_id
    JOIN pharma_item itm 
        ON i.item_id = itm.item_id
    JOIN pharma_supplier sup 
        ON s.supplier_id = sup.supplier_id
    WHERE i.expiry_date >= date_trunc('month', CURRENT_DATE + interval '1 month')
      AND i.expiry_date < date_trunc('month', CURRENT_DATE + interval '4 month')
      AND i.pharmacy_id = :pharmacyId
""", nativeQuery = true)
 List<ExpiredStockView> findNextThreeMonthsStockWithSupplier(@Param("pharmacyId") Long pharmacyId);


 @Query(value = """
        SELECT 
            itm.item_name AS itemName,
            itm.variant_name AS variantName,
            itm.unit_name AS unitName,
            i.batch_no AS batchNo,
            i.purchase_price_per_unit AS purchasePricePerUnit,
            i.mrp_sale_price_per_unit AS mrpSalePricePerUnit,
            i.package_quantity AS currentStock,
            i.created_date AS receivedDate,
            i.expiry_date AS expiryDate,
            sp.purchase_bill_no AS purchaseBillNo,
            sp.purchase_date AS purchaseDate,
            itm.manufacturer AS manufacturer,
            sup.supplier_name AS supplierName,
            (i.purchase_price_per_unit * i.package_quantity) AS value
        FROM pharma_inventory_details i
        LEFT JOIN pharma_stock_purchase_item spi
            ON i.item_id = spi.item_id
           AND i.batch_no = spi.batch_no
        LEFT JOIN pharma_stock_purchase sp
            ON spi.inv_id = sp.inv_id
        LEFT JOIN pharma_item itm
            ON i.item_id = itm.item_id
        LEFT JOIN pharma_supplier sup
            ON sp.supplier_id = sup.supplier_id
        WHERE i.pharmacy_id = :pharmacyId
        """,
         nativeQuery = true)
 List<InventoryView> getInventoryDetailsByPharmacy(@Param("pharmacyId") Long pharmacyId);

}