package com.project.pharma.repository;

import com.project.pharma.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockEntity, Integer> {
}
