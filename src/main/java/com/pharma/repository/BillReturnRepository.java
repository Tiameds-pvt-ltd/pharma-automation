package com.pharma.repository;

import com.pharma.dto.BillReturnListDto;
import com.pharma.entity.BillReturnEntity;
import com.pharma.entity.SupplierEntity;
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


    @Query("SELECT new com.pharma.dto.BillReturnListDto(" +
            "b.billReturnId, b.billReturnId1, b.billId1, b.billReturnDateTime, " +
            "b.grandTotal, b.patientType, b.patientId, " +
            "CONCAT(p.firstName, ' ', p.lastName), " +
            "COUNT(i.billReturnItemId)) " +
            "FROM BillReturnEntity b " +
            "LEFT JOIN b.billReturnItemEntities i " +
            "JOIN PatientDetailsEntity p ON b.patientId = p.patientId " +
            "WHERE b.pharmacyId = :pharmacyId " +
            "GROUP BY b.billReturnId, b.billReturnId1, b.billId1, b.billReturnDateTime, " +
            "b.grandTotal, b.patientType, b.patientId, p.firstName, p.lastName")
    List<BillReturnListDto> fetchBillReturnListsByPharmacyId(@Param("pharmacyId") Long pharmacyId);

    List<BillReturnEntity> findAllByPharmacyId(Long pharmacyId);

    Optional<BillReturnEntity> findByBillReturnIdAndPharmacyId(UUID billReturnId, Long pharmacyId);

    @Query(value = """
    SELECT *
    FROM pharma_billing_return
    WHERE pharmacy_id = :pharmacyId
      AND bill_return_id1 LIKE CONCAT('BILLRTN-', :year, '-%')
    ORDER BY CAST(SPLIT_PART(bill_return_id1, '-', 3) AS INTEGER) DESC
    LIMIT 1
""", nativeQuery = true)
    Optional<BillReturnEntity> findLatestBillReturnForYearAndPharmacy(
            @Param("pharmacyId") Long pharmacyId,
            @Param("year") String year
    );

}

