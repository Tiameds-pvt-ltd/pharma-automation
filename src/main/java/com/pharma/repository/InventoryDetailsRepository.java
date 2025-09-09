package com.pharma.repository;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.entity.InventoryDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
