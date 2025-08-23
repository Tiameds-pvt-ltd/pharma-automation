package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private UUID itemId;
    private String itemName;
    private Long purchaseUnit;
    private String variantName;
    private String unitName;
    private String manufacturer;
    private BigDecimal purchasePrice;
    private BigDecimal mrpSalePrice;
    private BigDecimal purchasePricePerUnit;
    private BigDecimal mrpSalePricePerUnit;
    private Long gstPercentage;
    private String genericName;
    private String hsnNo;
    private String consumables;
    private Long pharmacyId;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
