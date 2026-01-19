package com.pharma.dto;

import java.math.BigDecimal;

public interface ItemProfitSummaryDto {

    String getItemName();
    String getHsnNo();
    BigDecimal getGstPercentage();

    Long getTotalPackageQty();

    BigDecimal getNetSaleAmount();
    BigDecimal getGrossSaleAmount();

    BigDecimal getNetCostPrice();
    BigDecimal getGrossCostPrice();

    BigDecimal getNetProfit();
    BigDecimal getGrossProfit();
}
