package com.pharma.repository;

import com.pharma.entity.StockItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockItemRepository extends JpaRepository<StockItemEntity, UUID> {

    List<StockItemEntity> findByItemIdAndPharmacyId(UUID itemId, Long pharmacyId);

    @Query("SELECT s FROM StockItemEntity s " +
            "WHERE s.stockEntity.supplierId = :supplierId " +
            "AND s.pharmacyId = :pharmacyId")
    List<StockItemEntity> findItemsBySupplierIdAndPharmacyId(
            @Param("supplierId") UUID supplierId,
            @Param("pharmacyId") Long pharmacyId
    );


    @Query("SELECT s FROM StockItemEntity s " +
            "WHERE s.stockEntity.invId = :invId " +
            "AND s.itemId = :itemId " +
            "AND s.batchNo = :batchNo " +
            "AND s.pharmacyId = :pharmacyId")
    Optional<StockItemEntity> findByInvIdItemIdBatchNoAndPharmacyId(
            @Param("invId") UUID invId,
            @Param("itemId") UUID itemId,
            @Param("batchNo") String batchNo,
            @Param("pharmacyId") Long pharmacyId);



//    List<StockItemEntity> findByItemIdAndCreatedBy(UUID itemId, Long createdBy);

//    @Query("SELECT s FROM StockItemEntity s WHERE s.stockEntity.supplierId = :supplierId")

//    List<StockItemEntity> findItemsBySupplierId(@Param("supplierId") UUID supplierId);

//Optional<StockItemEntity> findByStockEntity_InvIdAndItemIdAndBatchNo(UUID invId, UUID itemId, String batchNo);

}
