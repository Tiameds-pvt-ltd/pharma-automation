package com.pharma.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ItemPatientBillDto {

    String getPatientName();
    String getBillId1();
    LocalDateTime getBillDateTime();
    String getBatchNo();
    Long getPackageQuantity();
    BigDecimal getNetTotal();
    BigDecimal getGstPercentage();
    BigDecimal getGstAmount();
    BigDecimal getGrossTotal();
}
