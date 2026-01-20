package com.pharma.dto;

import java.math.BigDecimal;

public interface ItemProfitByDoctorDto {

    String getItemName();
    BigDecimal getGstPercentage();
    Long getQuantity();
    BigDecimal getSalePrice();
    BigDecimal getPurchasePrice();
    BigDecimal getGstAmount();
    BigDecimal getProfit();
}
