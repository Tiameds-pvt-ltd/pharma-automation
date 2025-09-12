//package com.pharma.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.GenericGenerator;
//
//import java.time.LocalDate;
//import java.util.UUID;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "pharma_stock_edit")
//public class StockEditEntity {
//
//    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name = "stock_edit_id", updatable = false, nullable = false, unique = true)
//    private UUID stockEditId;
//
//    @Column(name = "stock_edited_date")
//    private LocalDate stockEditedDate;
//
//    @Column(name = "adjustment_type")
//    private String adjustmentType;
//
//    @Column(name = "updated_stock_quantity")
//    private Long updatedStockQuantity;
//
//    @Column(name = "created_by")
//    private Long createdBy;
//
//    @Column(name = "created_date")
//    private LocalDate createdDate;
//
//    @Column(name = "modified_by")
//    private Long modifiedBy;
//
//    @Column(name = "modified_Date")
//    private LocalDate modifiedDate;
//
//    @PrePersist
//    public void generateUUID() {
//        if (stockEditId == null) {
//            stockEditId = UUID.randomUUID();
//        }
//    }
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "inv_item_id", nullable = false)
//    private InventoryDetailsEntity inventoryDetailsEntity;
//
//}
