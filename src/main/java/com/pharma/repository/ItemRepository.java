package com.pharma.repository;

import com.pharma.entity.DoctorEntity;
import com.pharma.entity.ItemEntity;
import com.pharma.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {

    List<ItemEntity> findAllByCreatedBy(Long createdBy);

    Optional<ItemEntity> findByItemIdAndCreatedBy(UUID itemId, Long createdBy);

    boolean existsByItemNameAndManufacturerAndPharmacyId(String itemName, String manufacturer, Long pharmacyId);

    List<ItemEntity> findAllByPharmacyId(Long pharmacyId);

    Optional<ItemEntity> findByItemIdAndPharmacyId(UUID itemId, Long pharmacyId);

}
