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
@Table(name = "pharma_billing_return_item")
public class BillReturnItemEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "bill_return_item_id", updatable = false, nullable = false, unique = true)
    private UUID billReturnItemId;

    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "package_quantity")
    private Long packageQuantity;

    @Column(name = "return_quantity")
    private Long returnQuantity;

    @Column(name = "mrp_sale_price_per_unit")
    private BigDecimal mrpSalePricePerUnit;

    @Column(name = "gst_percentage")
    private BigDecimal gstPercentage;

    @Column(name = "gst_amount")
    private BigDecimal gstAmount;

    @Column(name = "gross_total")
    private BigDecimal grossTotal;

    @Column(name = "net_total")
    private BigDecimal netTotal;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date", insertable = false)
    private LocalDate modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billReturnId")
    private BillReturnEntity billReturnEntity;

    @PrePersist
    public void generateUUID() {
        if (billReturnItemId == null) {
            billReturnItemId = UUID.randomUUID();
        }
    }
}
