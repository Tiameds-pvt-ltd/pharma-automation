package com.pharma.dto;

import java.time.LocalDate;
import java.util.UUID;

public interface ExpiredStockView {

    UUID getItemId();
    String getItemName();
    String getBatchNo();
    Long getPackageQuantity();
    LocalDate getExpiryDate();
    UUID getSupplierId();
    String getSupplierName();
}
