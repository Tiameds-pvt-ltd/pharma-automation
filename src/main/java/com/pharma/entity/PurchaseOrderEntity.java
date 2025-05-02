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
@Table(name = "pharma_purchase_order")
public class PurchaseOrderEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "order_id", updatable = false, nullable = false, unique = true)
    private UUID orderId;

    @Column(name = "order_id1")
    private String orderId1;

    @Column(name = "pharmacy_id")
    private UUID pharmacyId;

    @Column(name = "pharmacist_id")
    private UUID pharmacistId;

    @Column(name = "supplier_id")
    private UUID supplierId;

    @Column(name = "ordered_date")
    private LocalDate orderedDate;

    @Column(name = "intended_delivery_date")
    private LocalDate intendedDeliveryDate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "total_gst")
    private BigDecimal totalGst;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date")
    private LocalDate modifiedDate;

    @OneToMany(mappedBy = "purchaseOrderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderItemEntity> purchaseOrderItemEntities = new ArrayList<>();

    @PrePersist
    public void generateUUID() {
        if (orderId == null) {
            orderId = UUID.randomUUID();
        }

    }
}
