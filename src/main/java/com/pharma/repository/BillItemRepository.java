package com.pharma.repository;

import com.pharma.entity.BillItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillItemRepository extends JpaRepository<BillItemEntity, UUID> {


}
