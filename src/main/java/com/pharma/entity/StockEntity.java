package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_stock_purchase")
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invId;

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "purchase_bill_no")
    private String purchaseBillNo;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "credit_period")
    private Integer creditPeriod;

    @Column(name = "payment_due_date")
    private LocalDate paymentDueDate;

    @Column(name = "invoice_amount")
    private BigDecimal invoiceAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "total_gst")
    private BigDecimal totalGst;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "good_status")
    private String goodStatus;

    @OneToMany(mappedBy = "stockEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockItemEntity> stockItemEntities = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockEntity that = (StockEntity) o;
        return Objects.equals(invId, that.invId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invId);
    }

    @ManyToMany(mappedBy = "stockEntities", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<User> users = new HashSet<>();

    @PreRemove
    private void preRemove() {
        for (User user : users) {
            user.getStockEntities().remove(this);
        }
    }

}
