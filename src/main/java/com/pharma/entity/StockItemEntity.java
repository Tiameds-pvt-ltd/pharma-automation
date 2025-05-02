package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pharma_stock_purchase_item")
public class StockItemEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "stock_id", updatable = false, nullable = false, unique = true)
    private UUID stockId;

    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "pharmacy_id")
    private UUID pharmacyId;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "package_quantity")
    private Long packageQuantity;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "free_item")
    private Long freeItem;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;

    @Column(name = "mrp_sale_price")
    private BigDecimal mrpSalePrice;

    @Column(name = "purchase_price_per_unit")
    private BigDecimal purchasePricePerUnit;

    @Column(name = "mrp_sale_price_per_unit")
    private BigDecimal mrpSalePricePerUnit;

    @Column(name = "cgst_percentage")
    private Long cgstPercentage;

    @Column(name = "sgst_percentage")
    private Long sgstPercentage;

    @Column(name = "cgst_amount")
    private BigDecimal cgstAmount;

    @Column(name = "sgst_amount")
    private BigDecimal sgstAmount;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date")
    private LocalDate modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inv_id", nullable = false)
    private StockEntity stockEntity;

    @PrePersist
    public void generateUUID() {
        if (stockId == null) {
            stockId = UUID.randomUUID();
        }
    }


//    public Integer getId() {
//        return stockId;
//    }
}
