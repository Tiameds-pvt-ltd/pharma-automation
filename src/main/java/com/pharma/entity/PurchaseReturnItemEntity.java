package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_purchase_return_item")
public class PurchaseReturnItemEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "return_item_id", updatable = false, nullable = false, unique = true)
    private UUID returnItemId;

    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "return_type")
    private String returnType;

    @Column(name = "return_quantity")
    private Long returnQuantity;

    @Column(name = "discrepancy_in")
    private String discrepancyIn;

    @Column(name = "discrepancy")
    private String discrepancy;

//    @Column(name = "expiry_date")
//    private LocalDate expiryDate;
//
//    @Column(name = "free_item")
//    private Long freeItem;
//
//    @Column(name = "discount")
//    private BigDecimal discount;
//
//    @Column(name = "purchase_price")
//    private BigDecimal purchasePrice;
//
//    @Column(name = "mrp_sale_price")
//    private BigDecimal mrpSalePrice;
//
//    @Column(name = "purchase_price_per_unit")
//    private BigDecimal purchasePricePerUnit;
//
//    @Column(name = "mrp_sale_price_per_unit")
//    private BigDecimal mrpSalePricePerUnit;
//
//    @Column(name = "cgst_percentage")
//    private Long cgstPercentage;
//
//    @Column(name = "sgst_percentage")
//    private Long sgstPercentage;
//
//    @Column(name = "cgst_amount")
//    private BigDecimal cgstAmount;
//
//    @Column(name = "sgst_amount")
//    private BigDecimal sgstAmount;
//
//    @Column(name = "amount")
//    private BigDecimal amount;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date", insertable = false)
    private LocalDate modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false)
    private PurchaseReturnEntity purchaseReturnEntity;

    @PrePersist
    public void generateUUID() {
        if (returnItemId == null) {
            returnItemId = UUID.randomUUID();
        }
    }
}
