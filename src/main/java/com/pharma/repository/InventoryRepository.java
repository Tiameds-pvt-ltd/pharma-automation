package com.pharma.repository;

import com.pharma.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository <InventoryEntity, Long> {

    Optional<InventoryEntity> findByItemId(String itemId);
}
