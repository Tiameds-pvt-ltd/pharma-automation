package com.pharma.repository;

import com.pharma.entity.DoctorEntity;
import com.pharma.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, UUID> {

    List<ItemEntity> findAllByCreatedBy(Long createdBy);

    Optional<ItemEntity> findByItemIdAndCreatedBy(UUID itemId, Long createdBy);
}
