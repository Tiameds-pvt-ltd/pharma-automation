package com.pharma.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DailySalesCostProfitDto {

    LocalDate getBillDate();

    Long getTotalQuantitySold();

    BigDecimal getSalesPrice();

    BigDecimal getCostPrice();

    BigDecimal getProfit();
}

