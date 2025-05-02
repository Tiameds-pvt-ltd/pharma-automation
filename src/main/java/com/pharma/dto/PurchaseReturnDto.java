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
public class PurchaseReturnDto {

    private UUID returnId;
    private String returnId1;
    private UUID invId;
    private LocalDate returnDate;
    private UUID supplierId;
    private String purchaseBillNo;
    private BigDecimal returnAmount;
    private UUID pharmacyId;
    private String remark;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

    private List<PurchaseReturnItemDto> purchaseReturnItemDtos = new ArrayList<>();

}
