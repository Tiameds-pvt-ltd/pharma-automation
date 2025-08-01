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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_billing")
public class BillEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "bill_id", updatable = false, nullable = false, unique = true)
    private UUID billId;

    @Column(name = "bill_id1")
    private String billId1;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;

    @Column(name = "bill_date_time", updatable = false)
    private LocalDateTime billDateTime;

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

    @Column(name = "total_discount")
    private BigDecimal totalDiscount;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "received_amount")
    private BigDecimal receivedAmount;

    @Column(name = "balance_amount")
    private BigDecimal balanceAmount;

    @Column(name = "upi")
    private BigDecimal upi;

    @Column(name = "cash")
    private BigDecimal cash;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date", insertable = false)
    private LocalDate modifiedDate;

    @OneToMany(mappedBy = "billEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillItemEntity> billItemEntities = new ArrayList<>();

    @PrePersist
    public void generateUUID() {
        if (billId == null) {
            billId = UUID.randomUUID();
        }
        this.billDateTime = LocalDateTime.now();
    }
}


