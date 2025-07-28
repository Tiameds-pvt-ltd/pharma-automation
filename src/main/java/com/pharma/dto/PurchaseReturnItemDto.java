package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseReturnItemDto {

    private UUID returnItemId;
    private UUID itemId;
    private String batchNo;
    private String returnType;
    private Long returnQuantity;
    private Long gstPercentage;
    private String discrepancyIn;
    private String discrepancy;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
