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
public class PurchaseOrderDto {

    private UUID orderId;
    private String orderId1;
    private Long pharmacyId;
    private UUID supplierId;
    private LocalDate orderedDate;
    private LocalDate intendedDeliveryDate;
    private BigDecimal totalAmount;
    private BigDecimal totalGst;
    private BigDecimal grandTotal;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

    private List<PurchaseOrderItemDto> purchaseOrderItemDtos = new ArrayList<>();
}
