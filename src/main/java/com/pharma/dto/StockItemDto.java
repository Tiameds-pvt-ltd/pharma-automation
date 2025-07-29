package com.pharma.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockItemDto {
    private UUID stockId;
    private UUID itemId;
    private String batchNo;
    private Long packageQuantity;
    private LocalDate expiryDate;
    private Long freeItem;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private BigDecimal purchasePrice;
    private BigDecimal mrpSalePrice;
    private BigDecimal purchasePricePerUnit;
    private BigDecimal mrpSalePricePerUnit;
    private Long gstPercentage;
    private BigDecimal gstAmount;
    private BigDecimal amount;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
