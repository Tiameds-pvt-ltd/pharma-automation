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
public class StockSummaryDto {

    private UUID invId;
    private UUID supplierId;
    private Long pharmacyId;
    private String purchaseBillNo;
    private LocalDate purchaseDate;
    private Long creditPeriod;
    private LocalDate paymentDueDate;
    private BigDecimal invoiceAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalCgst;
    private BigDecimal totalSgst;
    private BigDecimal totalDiscountPercentage;
    private BigDecimal totalDiscountAmount;
    private BigDecimal grandTotal;
    private String paymentStatus;
    private String goodStatus;
    private String grnNo;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;
}

