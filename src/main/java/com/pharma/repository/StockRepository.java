package com.pharma.repository;


import com.pharma.entity.PurchaseOrderEntity;
import com.pharma.entity.StockEntity;
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

    Optional<StockEntity> findByInvIdAndCreatedBy(UUID invId, Long createdBy);

    @Query("SELECT p.purchaseBillNo FROM StockEntity p WHERE p.supplierId = :supplierId AND EXTRACT(YEAR FROM p.purchaseDate) = :year AND LOWER(p.purchaseBillNo) = LOWER(:purchaseBillNo)")
    List<String> findBillNoBySupplierIdAndYear(@Param("supplierId") UUID supplierId,
                                               @Param("year") int year,
                                               @Param("purchaseBillNo") String purchaseBillNo);


    @Query("SELECT s FROM StockEntity s WHERE s.grnNo LIKE CONCAT('GRN-', :date, '-%') ORDER BY s.grnNo DESC LIMIT 1")
    Optional<StockEntity> findLatestGrnNo(@Param("date") String date);

}
