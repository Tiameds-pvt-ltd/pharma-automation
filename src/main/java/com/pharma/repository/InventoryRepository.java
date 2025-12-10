package com.pharma.repository;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.entity.InventoryEntity;
import com.pharma.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository <InventoryEntity, UUID> {

    Optional<InventoryEntity> findByItemId(UUID itemId);

//    @Query("SELECT p.itemId, SUM(p.packageQuantity) " +
//            "FROM StockItemEntity p " +
//            "JOIN InventoryEntity i ON p.itemId = i.itemId " +
//            "WHERE p.expiryDate < CURRENT_DATE " +
//            "GROUP BY p.itemId")
//    List<Object[]> getExpiredStock(Long createdBy);

    @Query("""
    SELECT p.itemId, SUM(p.packageQuantity)
    FROM StockItemEntity p
    WHERE p.expiryDate < CURRENT_DATE
      AND p.pharmacyId = :pharmacyId
    GROUP BY p.itemId
""")
    List<Object[]> getExpiredStockByPharmacy(@Param("pharmacyId") Long pharmacyId);


    List<InventoryEntity> findAllByCreatedBy(Long createdBy);

//    @Query("""
//    SELECT new com.pharma.dto.ExpiredStockDto(
//        i.itemId,
//        itm.itemName,
//        i.batchNo,
//        i.packageQuantity,
//        i.expiryDate,
//        s.supplierId,
//        sup.supplierName
//    )
//    FROM InventoryDetailsEntity i
//    JOIN StockItemEntity e ON i.itemId = e.itemId AND i.batchNo = e.batchNo
//    JOIN e.stockEntity s
//    JOIN ItemEntity itm ON i.itemId = itm.itemId
//    JOIN SupplierEntity sup ON s.supplierId = sup.supplierId
//    WHERE i.expiryDate <= CURRENT_DATE
//      AND i.createdBy = :createdById
//""")
//    List<ExpiredStockDto> findExpiredStockWithSupplier(@Param("createdById") Long createdById);

    @Query("""
    SELECT new com.pharma.dto.ExpiredStockDto(
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
    WHERE i.expiryDate <= CURRENT_DATE
      AND i.pharmacyId = :pharmacyId
""")
    List<ExpiredStockDto> findExpiredStockWithSupplier(@Param("pharmacyId") Long pharmacyId);


    List<InventoryEntity> findAllByPharmacyId(Long pharmacyId);

}
