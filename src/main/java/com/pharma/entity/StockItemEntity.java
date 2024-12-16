package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pharma_stock_purchase_item")
public class StockItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stockId;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "batch_no")
    private String batchNo;

    @Column(name = "package_quantity")
    private Integer packageQuantity;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "free_item")
    private Integer freeItem;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "gst_percentage")
    private Integer gstPercentage;

    @Column(name = "gst_amount")
    private Double gstAmount;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invId")
    private StockEntity stockEntity;


    public Integer getId() {
        return stockId;
    }
}
