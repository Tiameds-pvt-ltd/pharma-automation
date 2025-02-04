package com.pharma.repository;


import com.pharma.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {

    @Query("SELECT p.purchaseBillNo FROM StockEntity p WHERE p.supplierId = :supplierId AND EXTRACT(YEAR FROM p.purchaseDate) = :year AND LOWER(p.purchaseBillNo) = LOWER(:purchaseBillNo)")
    List<String> findBillNoBySupplierIdAndYear(@Param("supplierId") Long supplierId,
                                               @Param("year") int year,
                                               @Param("purchaseBillNo") String purchaseBillNo);



}
