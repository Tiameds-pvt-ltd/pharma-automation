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
@Table(name = "pharma_purchase_order_item")
public class PurchaseOrderItemEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "order_item_id", updatable = false, nullable = false, unique = true)
    private UUID orderItemId;

    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "package_quantity")
    private Long packageQuantity;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "gst_percentage")
    private Long gstPercentage;

    @Column(name = "gst_amount")
    private BigDecimal gstAmount;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date", insertable = false)
    private LocalDate modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private PurchaseOrderEntity purchaseOrderEntity;

    @PrePersist
    public void generateUUID() {
        if (orderItemId == null) {
            orderItemId = UUID.randomUUID();
        }
    }
}
