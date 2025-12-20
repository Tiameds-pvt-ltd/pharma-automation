package com.pharma.repository;

import java.math.BigDecimal;
import java.util.UUID;

public interface BatchWiseProfitDto {

    Long getPharmacyId();

    UUID getItemId();

    String getItemName();

    String getBatchNo();

    BigDecimal getBilledAmount();

    BigDecimal getCostPrice();

    BigDecimal getGrossProfit();

    BigDecimal getGrossProfitPercentage();
}

