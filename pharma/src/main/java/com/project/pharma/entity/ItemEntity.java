package com.project.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pharma_item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "purchase_unit")
    private String purchaseUnit;

    @Column(name = "unit_type")
    private String unitType;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "purchase_price")
    private Integer purchasePrice;

    @Column(name = "mrp_sale_price")
    private Integer mrpSalePrice;

    @Column(name = "purchase_price_per_unit")
    private Integer purchasePricePerUnit;

    @Column(name = "mrp_sale_price_per_unit")
    private Integer mrpSalePricePerUnit;

    @Column(name = "gst_percentage")
    private String gstPercentage;

    @Column(name = "hsn_no")
    private String hsnNo;

    @Column(name = "consumables")
    private String consumables;

}
