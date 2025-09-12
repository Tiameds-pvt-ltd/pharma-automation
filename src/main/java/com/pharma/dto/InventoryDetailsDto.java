package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDetailsDto {

    private UUID invDetailsId;
    private UUID itemId;
    private String batchNo;
    private Long packageQuantity;
    private LocalDate expiryDate;
    private BigDecimal purchasePrice;
    private BigDecimal mrpSalePrice;
    private BigDecimal purchasePricePerUnit;
    private BigDecimal mrpSalePricePerUnit;
    private Long gstPercentage;
    private BigDecimal gstAmount;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

//    private List<StockEditDto> stockEditDtos = new ArrayList<>();
}
