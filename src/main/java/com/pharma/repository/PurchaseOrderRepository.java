package com.pharma.repository;

import com.pharma.entity.PurchaseOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, UUID> {

    List<PurchaseOrderEntity> findAllByCreatedBy(Long createdBy);

    Optional<PurchaseOrderEntity> findByOrderIdAndCreatedBy(UUID orderId, Long createdBy);

    @Query("SELECT p FROM PurchaseOrderEntity p WHERE p.orderId1 LIKE CONCAT('ORD-', :date, '-%') ORDER BY p.orderId1 DESC LIMIT 1")
    Optional<PurchaseOrderEntity> findLatestOrderForToday(@Param("date") String date);

}
