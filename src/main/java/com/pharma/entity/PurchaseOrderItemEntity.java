package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_purchase_order_item")
public class PurchaseOrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "purchase_bill_no")
    private String purchaseBillNo;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "gst_percentage")
    private Integer gstPercentage;

    @Column(name = "gst_amount")
    private Double gstAmount;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "unit_type_id")
    private Long unitTypeId;

    @Column(name = "variant_type_id")
    private Long variantTypeId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date")
    private LocalDate modifiedDate;



}
