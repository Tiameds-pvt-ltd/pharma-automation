package com.project.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pharma_stock_purchase")
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invId;

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "store")
    private String store;

    @Column(name = "purchase_bill_no")
    private String purchaseBillNo;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "credit_period")
    private Integer creditPeriod;

    @Column(name = "payment_due_date")
    private Date paymentDueDate;

    @Column(name = "invoice_amount")
    private Integer invoiceAmount;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "total_gst")
    private Double totalGst;

    @Column(name = "total_discount")
    private Double totalDiscount;

    @Column(name = "grand_total")
    private Double grandTotal;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "good_status")
    private String goodStatus;

    @OneToMany(mappedBy = "stockEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockItemEntity> stockItemEntities= new ArrayList<>();


}
