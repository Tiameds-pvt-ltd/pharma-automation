package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_billing_item")
public class BillItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billItemId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "mrp")
    private BigDecimal mrp;

    @Column(name = "gst_percentage")
    private BigDecimal gstPercentage;

    @Column(name = "gross_total")
    private BigDecimal grossTotal;

    @Column(name = "net_total")
    private BigDecimal netTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billId")
    private BillEntity billEntity;
}
