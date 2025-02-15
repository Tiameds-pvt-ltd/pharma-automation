package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemDto {

    private Long orderItemId;
    private Long orderId;
    private Long itemId;
    private String purchaseBillNo;
    private String manufacturer;
    private Integer gstPercentage;
    private Double gstAmount;
    private Double amount;
    private Long unitTypeId;
    private Long variantTypeId;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;
}
