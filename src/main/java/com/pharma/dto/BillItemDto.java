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
public class BillItemDto {

    private UUID billItemId;
    private Long billId;
    private UUID itemId;
    private String batchNo;
    private LocalDate expiryDate;
    private Long packageQuantity;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    private BigDecimal mrpSalePricePerUnit;
    private BigDecimal gstPercentage;
    private BigDecimal gstAmount;
    private BigDecimal grossTotal;
    private BigDecimal netTotal;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
