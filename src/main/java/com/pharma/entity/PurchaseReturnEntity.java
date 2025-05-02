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
@Table(name = "pharma_purchase_return")
public class PurchaseReturnEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "return_id", updatable = false, nullable = false, unique = true)
    private UUID returnId;

    @Column(name = "return_id1")
    private String returnId1;

    @Column(name = "inv_id")
    private UUID invId;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "supplier_id")
    private UUID supplierId;

    @Column(name = "purchase_bill_no")
    private String purchaseBillNo;

    @Column(name = "return_amount")
    private BigDecimal returnAmount;

    @Column(name = "pharmacy_id")
    private UUID pharmacyId;

    @Column(name = "remark")
    private String remark;
//    @Column(name = "return_reason")
//    private String returnReason;
//
//    @Column(name = "return_type")
//    private String returnType;
//
//    @Column(name = "refund_type")
//    private String refundType;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date")
    private LocalDate modifiedDate;

    @OneToMany(mappedBy = "purchaseReturnEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseReturnItemEntity> purchaseReturnItemEntities = new ArrayList<>();

    @PrePersist
    public void generateUUID() {
        if (returnId == null) {
            returnId = UUID.randomUUID();
        }

    }
}
