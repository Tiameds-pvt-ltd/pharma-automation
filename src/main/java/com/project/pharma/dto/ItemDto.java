package com.project.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Integer itemId;
    private String itemName;
    private String purchaseUnit;
    private String unitType;
    private String manufacturer;
    private Integer purchasePrice;
    private Integer mrpSalePrice;
    private Integer purchasePricePerUnit;
    private Integer mrpSalePricePerUnit;
    private String gstPercentage;
    private String hsnNo;
    private String consumables;

}
