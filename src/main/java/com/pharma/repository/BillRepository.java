package com.pharma.repository;

import com.pharma.entity.BillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
}
