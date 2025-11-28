package com.pharma.repository;

import com.pharma.dto.BillReturnListDto;
import com.pharma.entity.BillReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillReturnRepository extends JpaRepository<BillReturnEntity, UUID> {

    List<BillReturnEntity> findAllByCreatedBy(Long createdBy);

    Optional<BillReturnEntity> findByBillReturnIdAndCreatedBy(UUID billReturnId, Long createdBy);

    @Query("SELECT p FROM BillReturnEntity p WHERE p.billReturnId1 LIKE CONCAT('BILLRTN-', :year, '-%') ORDER BY p.billReturnId1 DESC LIMIT 1")
    Optional<BillReturnEntity> findLatestBillReturnForYear(@Param("year") String year);


    @Query("SELECT new com.pharma.dto.BillReturnListDto(" +
            "b.billReturnId, b.billReturnId1, b.billId1, b.billReturnDateTime, " +
            "b.grandTotal, b.patientType, b.patientId, " +
            "(p.patientName), " +
            "COUNT(i.billReturnItemId)) " +
            "FROM BillReturnEntity b " +
            "LEFT JOIN b.billReturnItemEntities i " +
            "JOIN PatientDetailsEntity p ON b.patientId = p.patientId " +
            "WHERE b.createdBy = :createdBy " +
            "GROUP BY b.billReturnId, b.billReturnId1, b.billId1, b.billReturnDateTime, " +
            "b.grandTotal, b.patientType, b.patientId, p.patientName")
    List<BillReturnListDto> fetchBillReturnListsByCreatedBy(Long createdBy);
}

