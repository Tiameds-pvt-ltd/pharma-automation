package com.project.pharma.repository;

import com.project.pharma.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
}
