package com.pharma.dto;

import java.math.BigDecimal;

public interface PaymentSummaryDto {

    BigDecimal getCardTotal();
    Long getCardCount();

    BigDecimal getUpiTotal();
    Long getUpiCount();

    BigDecimal getNetBankingTotal();
    Long getNetBankingCount();

    BigDecimal getCashTotal();
    Long getCashCount();

    BigDecimal getChequeTotal();
    Long getChequeCount();
}
