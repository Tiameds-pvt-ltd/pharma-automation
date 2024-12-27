package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_billing")
public class BillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    @Column(name = "bill_date")
    private LocalDate billDate;

    @Column(name = "bill_time")
    private LocalTime billTime;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "doctor_id")
    private Long doctorId;

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

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "bill_no")
    private Long billNo;

    @Column(name = "entered_by")
    private Long enteredBy;

    @PrePersist
    protected void onCreate() {
        this.billDate = LocalDate.now();
        this.billTime = LocalTime.now();
    }

    @OneToMany(mappedBy = "billEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillItemEntity> billItemEntities = new ArrayList<>();
}


