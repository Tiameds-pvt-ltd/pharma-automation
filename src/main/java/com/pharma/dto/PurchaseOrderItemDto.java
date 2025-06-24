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
public class PurchaseOrderItemDto {

    private UUID orderItemId;
    private UUID orderId;
    private UUID itemId;
    private Long packageQuantity;
    private String manufacturer;
    private Long gstPercentage;
    private BigDecimal gstAmount;
    private BigDecimal amount;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;
}
