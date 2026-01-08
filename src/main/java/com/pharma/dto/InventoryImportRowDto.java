package com.pharma.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class InventoryImportRowDto {

    private String itemName;
    private String batchNo;
    private Long packageQuantity;
    private LocalDate expiryDate;
    private BigDecimal purchasePrice;
    private BigDecimal mrpSalePrice;
    private Long gstPercentage;
}

