package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_purchase_order")
public class PurchaseOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;

    @Column(name = "pharmacist_id")
    private Long pharmacistId;

    @Column(name = "supplier_id")
    private Long supplierId;

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


}
