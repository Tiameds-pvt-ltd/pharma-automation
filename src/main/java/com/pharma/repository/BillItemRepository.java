package com.pharma.repository;

import com.pharma.entity.BillItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillItemRepository extends JpaRepository<BillItemEntity, Long> {
}
