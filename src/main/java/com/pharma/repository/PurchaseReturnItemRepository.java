package com.pharma.repository;

import com.pharma.entity.PurchaseReturnItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchaseReturnItemRepository extends JpaRepository<PurchaseReturnItemEntity, UUID> {

}
