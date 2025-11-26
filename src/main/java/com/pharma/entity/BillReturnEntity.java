package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_billing_return")
public class BillReturnEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "bill_return_id", updatable = false, nullable = false, unique = true)
    private UUID billReturnId;

    @Column(name = "bill_return_id1")
    private String billReturnId1;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;

    @Column(name = "bill_return_date_time", updatable = false)
    private LocalDateTime billReturnDateTime;

    @Column(name = "bill_id1")
    private String billId1;

    @Column(name = "patient_id")
    private UUID patientId;

    @Column(name = "doctor_id")
    private UUID doctorId;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "patient_type")
    private String patientType;

    @Column(name = "sub_total")
    private BigDecimal subTotal;

    @Column(name = "total_gst")
    private BigDecimal totalGst;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Column(name = "return_reason")
    private String returnReason;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date", insertable = false)
    private LocalDate modifiedDate;

    @OneToMany(mappedBy = "billReturnEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillReturnItemEntity> billReturnItemEntities = new ArrayList<>();

    @PrePersist
    public void generateUUID() {
        if (billReturnId == null) {
            billReturnId = UUID.randomUUID();
        }
        this.billReturnDateTime = LocalDateTime.now();
    }
}
