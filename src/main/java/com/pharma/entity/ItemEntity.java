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
@Table(name="pharma_item")
public class ItemEntity {

     @Id
     @GeneratedValue(generator = "UUID")
     @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
     @Column(name = "item_id", updatable = false, nullable = false, unique = true)
     private UUID itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "purchase_unit")
    private String purchaseUnit;

    @Column(name = "unit_id")
    private UUID unitId;

    @Column(name = "variant_id")
    private UUID variantId;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;

    @Column(name = "mrp_sale_price")
    private BigDecimal mrpSalePrice;

    @Column(name = "purchase_price_per_unit")
    private BigDecimal purchasePricePerUnit;

    @Column(name = "mrp_sale_price_per_unit")
    private BigDecimal mrpSalePricePerUnit;

    @Column(name = "cgst_percentage")
    private Long cgstPercentage;

    @Column(name = "sgst_percentage")
    private Long sgstPercentage;

    @Column(name = "hsn_no")
    private String hsnNo;

    @Column(name = "consumables")
    private String consumables;

     @Column(name = "created_by")
     private Long createdBy;

     @Column(name = "created_date")
     private LocalDate createdDate;

     @Column(name = "modified_by")
     private Long modifiedBy;

     @Column(name = "modified_Date")
     private LocalDate modifiedDate;

     @PrePersist
     public void generateUUID() {
         if (itemId == null) {
             itemId = UUID.randomUUID();
         }
     }

}
