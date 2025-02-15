package com.pharma.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private Integer purchasePrice;
    private Integer mrpSalePrice;
    private Integer gstPercentage;
    private Double gstAmount;
    private Double amount;
    private String store;

}
