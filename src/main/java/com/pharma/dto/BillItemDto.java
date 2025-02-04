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
public class BillItemDto {

    private Long billItemId;
    private Long billId;
    private Long itemId;
    private String batchNo;
    private LocalDate expiryDate;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal mrp;
    private BigDecimal gstPercentage;
    private BigDecimal grossTotal;
    private BigDecimal netTotal;

}
