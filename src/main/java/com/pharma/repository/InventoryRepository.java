package com.pharma.repository;

import com.pharma.entity.InventoryEntity;
import com.pharma.entity.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository <InventoryEntity, UUID> {

    Optional<InventoryEntity> findByItemId(UUID itemId);

    @Query("SELECT p.itemId, SUM(p.packageQuantity) " +
            "FROM StockItemEntity p " +
            "JOIN InventoryEntity i ON p.itemId = i.itemId " +
            "WHERE p.expiryDate < CURRENT_DATE " +
            "GROUP BY p.itemId")
    List<Object[]> getExpiredStock(Long createdBy);


    List<InventoryEntity> findAllByCreatedBy(Long createdBy);


}
