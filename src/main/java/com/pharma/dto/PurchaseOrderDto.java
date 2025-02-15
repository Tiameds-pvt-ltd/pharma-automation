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
public class PurchaseOrderDto {

    private Long orderId;
    private Long pharmacyId;
    private Long pharmacistId;
    private Long supplierId;
    private LocalDate orderedDate;
    private LocalDate intendedDeliveryDate;
    private BigDecimal totalAmount;
    private BigDecimal totalGst;
    private BigDecimal grandTotal;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;
}
