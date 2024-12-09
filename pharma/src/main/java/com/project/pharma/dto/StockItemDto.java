package com.project.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockItemDto {

    private Integer stockId;
    private String itemId;
    private String batchNo;
    private Integer packageQuantity;
    private Date expiryDate;
    private Integer freeItem;
    private Integer discount;
    private Integer gstPercentage;
    private Double gstAmount;
    private Double amount;

}
