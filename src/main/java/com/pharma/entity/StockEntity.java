//package com.pharma.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name="pharma_stock_purchase")
//public class StockEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long invId;
//
//    @Column(name = "supplier_id")
//    private String supplierId;
//
//    @Column(name = "store")
//    private String store;
//
//    @Column(name = "purchase_bill_no")
//    private String purchaseBillNo;
//
//    @Column(name = "purchase_date")
//    private Date purchaseDate;
//
//    @Column(name = "credit_period")
//    private Integer creditPeriod;
//
//    @Column(name = "payment_due_date")
//    private Date paymentDueDate;
//
//    @Column(name = "invoice_amount")
//    private Integer invoiceAmount;
//
//    @Column(name = "total_amount")
//    private Double totalAmount;
//
//    @Column(name = "total_gst")
//    private Double totalGst;
//
//    @Column(name = "total_discount")
//    private Double totalDiscount;
//
//    @Column(name = "grand_total")
//    private Double grandTotal;
//
//    @Column(name = "payment_status")
//    private String paymentStatus;
//
//    @Column(name = "good_status")
//    private String goodStatus;
//
//    @OneToMany(mappedBy = "stockEntity", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<StockItemEntity> stockItemEntities= new ArrayList<>();
//
//
//}



//==================

package com.pharma.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @NotNull(message = "Supplier ID cannot be null")
    @Column(name = "supplier_id", nullable = false)
    private String supplierId;

    @NotNull(message = "Store cannot be null")
    @Column(name = "store", nullable = false)
    private String store;

    @NotNull(message = "Purchase Bill No cannot be null")
    @Column(name = "purchase_bill_no", nullable = false)
    private String purchaseBillNo;

    @NotNull(message = "Purchase Date cannot be null")
    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "credit_period")
    private Integer creditPeriod;

    @Column(name = "payment_due_date")
    private LocalDate paymentDueDate;

    @NotNull(message = "Invoice Amount cannot be null")
    @Column(name = "invoice_amount", nullable = false)
    private BigDecimal invoiceAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "total_gst")
    private BigDecimal totalGst;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "good_status", nullable = false)
    private GoodStatus goodStatus;

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

    // Enum for Payment Status
    public enum PaymentStatus {
        PENDING, PAID, OVERDUE
    }

    // Enum for Good Status
    public enum GoodStatus {
        RECEIVED, DAMAGED, RETURNED
    }
}
