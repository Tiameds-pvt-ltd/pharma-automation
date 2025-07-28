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

    @Column(name = "gst_percentage")
    private Long gstPercentage;

    @Column(name = "discrepancy_in")
    private String discrepancyIn;

    @Column(name = "discrepancy")
    private String discrepancy;

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
