package com.pharma.dto;

import java.math.BigDecimal;

public interface ItemProfitSummaryDto {

    String getItemName();
    String getHsnNo();
    BigDecimal getGstPercentage();

    Long getQuantity();

    BigDecimal getGrossSalePrice();
    BigDecimal getSaleGstAmount();
    BigDecimal getNetSalePrice();

    BigDecimal getGrossCostPrice();
    BigDecimal getCostGstAmount();
    BigDecimal getNetCostPrice();

    BigDecimal getGrossProfit();
    BigDecimal getNetProfit();
}
