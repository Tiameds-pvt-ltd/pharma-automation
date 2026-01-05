package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ItemProfitRowDto {

    private String batchNo;
    private LocalDateTime billDateTime;
    private String billId1;
    private Long packageQuantity;
    private BigDecimal salePrice;
    private BigDecimal costPrice;
    private BigDecimal grossProfit;
}
