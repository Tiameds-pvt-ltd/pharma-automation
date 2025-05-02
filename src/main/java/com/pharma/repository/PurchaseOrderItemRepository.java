package com.pharma.repository;

import com.pharma.entity.PurchaseOrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItemEntity, UUID> {
}
