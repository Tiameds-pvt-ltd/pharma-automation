package com.project.pharma.repository;

import com.project.pharma.entity.StockItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockItemRepository extends JpaRepository<StockItemEntity, Integer> {
}
