package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_supplier_payment")
public class SupplierPaymentEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "payment_id", updatable = false, nullable = false, unique = true)
    private UUID paymentId;

    @Column(name = "supplier_id")
    private UUID supplierId;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "reference_no")
    private String referenceNo;

    @Column(name = "amount_paid")
    private BigDecimal amountPaid;

    @Column(name = "remark")
    private String remark;

    @Column(name = "credit_note_amount")
    private BigDecimal creditNoteAmount;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date")
    private LocalDate modifiedDate;

    @OneToMany(mappedBy = "supplierPaymentEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierPaymentDetailsEntity> supplierPaymentDetailsEntities = new ArrayList<>();

    @PrePersist
    public void generateUUID() {
        if (paymentId == null) {
            paymentId = UUID.randomUUID();
        }
    }
}
