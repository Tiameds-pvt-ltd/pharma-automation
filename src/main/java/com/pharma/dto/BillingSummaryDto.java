package com.pharma.dto;

import java.math.BigDecimal;


public interface BillingSummaryDto {

    BigDecimal getTotalReturnAmount();
    Long getTotalReturnBills();
    BigDecimal getPaidTotalAmount();
    Long getPaidTotalBills();
    BigDecimal getUnpaidTotalAmount();
    Long getUnpaidTotalBills();
}
