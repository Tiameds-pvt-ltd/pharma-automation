package com.pharma.dto;
import java.math.BigDecimal;

public interface ItemProfitByDoctorDto {

    String getItemName();
    Long getTotalQuantitySold();
    BigDecimal getTotalSalesPrice();
    BigDecimal getTotalCostPrice();
    BigDecimal getProfit();
}
