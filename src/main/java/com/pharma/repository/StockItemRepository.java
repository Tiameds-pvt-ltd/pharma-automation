package com.pharma.repository;

import com.pharma.entity.StockItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockItemRepository extends JpaRepository<StockItemEntity, UUID> {

    List<StockItemEntity> findByItemIdAndCreatedBy(UUID itemId, Long createdBy);

    @Query("SELECT s FROM StockItemEntity s WHERE s.stockEntity.supplierId = :supplierId")
    List<StockItemEntity> findItemsBySupplierId(@Param("supplierId") UUID supplierId);

}
