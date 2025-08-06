package com.pharma.dto;

import java.math.BigDecimal;

public interface PaymentSummaryDto {

    BigDecimal getCardTotal();
    BigDecimal getUpiNetTotal();
    BigDecimal getCashTotal();
    BigDecimal getUpiCashTotal();
    Long getCardCount();
    Long getUpiNetCount();
    Long getCashCount();
    Long getUpiCashCount();
}
