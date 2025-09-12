package com.pharma.repository;

import com.pharma.entity.PurchaseOrderEntity;
import com.pharma.entity.SupplierPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierPaymentRepo extends JpaRepository <SupplierPaymentEntity, UUID> {

    List<SupplierPaymentEntity> findAllByCreatedBy(Long createdBy);

    Optional<SupplierPaymentEntity> findByPaymentIdAndCreatedBy(UUID paymentId, Long createdBy);

}
