package com.pharma.repository;


import com.pharma.entity.StockItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockItemRepository extends JpaRepository<StockItemEntity, Integer> {

    List<StockItemEntity> findByItemId(String itemId);
}
