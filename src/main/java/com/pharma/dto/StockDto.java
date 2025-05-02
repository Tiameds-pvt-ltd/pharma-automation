package com.pharma.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {

    private UUID invId;
    private UUID supplierId;
    private String purchaseBillNo;
    private LocalDate purchaseDate;
    private Long creditPeriod;
    private LocalDate paymentDueDate;
    private BigDecimal invoiceAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalCgst;
    private BigDecimal totalSgst;
    private BigDecimal totalDiscount;
    private BigDecimal grandTotal;
    private String paymentStatus="Pending";
    private String goodStatus="Received";
    private String grnNo;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

    private List<StockItemDto> stockItemDtos = new ArrayList<>();
}
