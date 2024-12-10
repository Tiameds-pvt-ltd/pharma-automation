package com.pharma.repository.auth;

import com.pharma.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {

}