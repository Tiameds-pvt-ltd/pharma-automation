package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_stock_purchase")
public class StockEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "inv_id", updatable = false, nullable = false, unique = true)
    private UUID invId;

    @Column(name = "supplier_id")
    private UUID supplierId;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;

    @Column(name = "purchase_bill_no")
    private String purchaseBillNo;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "credit_period")
    private Long creditPeriod;

    @Column(name = "payment_due_date")
    private LocalDate paymentDueDate;

    @Column(name = "invoice_amount")
    private BigDecimal invoiceAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "total_cgst")
    private BigDecimal totalCgst;

    @Column(name = "total_sgst")
    private BigDecimal totalSgst;

    @Column(name = "total_discount_percentage")
    private BigDecimal totalDiscountPercentage;

    @Column(name = "total_discount_amount")
    private BigDecimal totalDiscountAmount;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "good_status")
    private String goodStatus;

    @Column(name = "grn_no")
    private String grnNo;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date")
    private LocalDate modifiedDate;

    @OneToMany(mappedBy = "stockEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockItemEntity> stockItemEntities = new ArrayList<>();

    @PrePersist
    public void generateUUID() {
        if (invId == null) {
            invId = UUID.randomUUID();
        }

    }


}
