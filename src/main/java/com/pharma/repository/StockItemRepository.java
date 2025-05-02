package com.pharma.repository;


import com.pharma.entity.PurchaseOrderEntity;
import com.pharma.entity.StockItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockItemRepository extends JpaRepository<StockItemEntity, UUID> {

//    List<StockItemEntity> findByItemId(UUID itemId);

    List<StockItemEntity> findByItemIdAndCreatedBy(UUID itemId, Long createdBy);

}
