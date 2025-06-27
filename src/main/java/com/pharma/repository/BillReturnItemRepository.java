package com.pharma.repository;

import com.pharma.entity.BillReturnItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillReturnItemRepository extends JpaRepository<BillReturnItemEntity, UUID> {
}
