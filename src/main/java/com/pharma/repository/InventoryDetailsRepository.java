package com.pharma.repository;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.ExpiredStockView;
import com.pharma.entity.InventoryDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryDetailsRepository extends JpaRepository<InventoryDetailsEntity, UUID> {

    Optional<InventoryDetailsEntity> findByItemIdAndBatchNo(UUID itemId, String batchNo);

    List<InventoryDetailsEntity> findAllByCreatedBy(Long createdBy);

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
        ON i.itemId = e.itemId AND i.batchNo = e.batchNo
    JOIN e.stockEntity s
    JOIN ItemEntity itm ON i.itemId = itm.itemId
    JOIN SupplierEntity sup ON s.supplierId = sup.supplierId
    WHERE FUNCTION('date_trunc', 'year', i.expiryDate) = FUNCTION('date_trunc', 'year', CURRENT_DATE)
      AND i.createdBy = :createdById
""")
    List<ExpiredStockDto> findCurrentYearStockWithSupplier(@Param("createdById") Long createdById);

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
        ON i.item_id = e.item_id AND i.batch_no = e.batch_no
    JOIN pharma_stock_purchase s ON e.inv_id = s.inv_id
    JOIN pharma_item itm ON i.item_id = itm.item_id
    JOIN pharma_supplier sup ON s.supplier_id = sup.supplier_id
    WHERE i.expiry_date >= date_trunc('month', CURRENT_DATE + interval '1 month')
      AND i.expiry_date < date_trunc('month', CURRENT_DATE + interval '4 month')
      AND i.created_by = :createdById
""", nativeQuery = true)
    List<ExpiredStockView> findNextThreeMonthsStockWithSupplier(@Param("createdById") Long createdById);


}