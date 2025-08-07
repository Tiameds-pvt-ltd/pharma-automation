package com.pharma.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BillingGstSummaryDto {
    String getBillId1();
    LocalDate getBillDate();
    BigDecimal getSubTotal();
    BigDecimal getTotalGst();
    BigDecimal getGrandTotal();
}