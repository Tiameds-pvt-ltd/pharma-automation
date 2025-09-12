package com.pharma.repository;

import com.pharma.entity.SupplierPaymentDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SupplierPaymentDetailsRepo extends JpaRepository<SupplierPaymentDetailsEntity, UUID> {
}
