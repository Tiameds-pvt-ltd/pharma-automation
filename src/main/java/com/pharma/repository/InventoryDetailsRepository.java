package com.pharma.repository;

import com.pharma.entity.InventoryDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryDetailsRepository extends JpaRepository<InventoryDetailsEntity, UUID> {

    Optional<InventoryDetailsEntity> findByItemIdAndBatchNo(UUID itemId, String batchNo);

    List<InventoryDetailsEntity> findAllByCreatedBy(Long createdBy);
}
