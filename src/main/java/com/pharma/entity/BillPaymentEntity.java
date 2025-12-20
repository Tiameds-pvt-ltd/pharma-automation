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
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_bill_payment")
public class BillPaymentEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "bill_payment_id", updatable = false, nullable = false, unique = true)
    private UUID billPaymentId;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "payment_date", updatable = false)
    private LocalDateTime paymentDate;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "return_amount")
    private BigDecimal returnAmount;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date", insertable = false)
    private LocalDate modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billId")
    private BillEntity billEntity;

    @PrePersist
    public void generateUUID() {
        if (billPaymentId == null) {
            billPaymentId = UUID.randomUUID();
        }

    }

}
