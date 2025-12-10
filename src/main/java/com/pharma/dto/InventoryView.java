package com.pharma.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InventoryView {

    String getItemName();
    String getVariantName();
    String getUnitName();
    String getBatchNo();
    BigDecimal getPurchasePricePerUnit();
    BigDecimal getMrpSalePricePerUnit();
    Long getCurrentStock();
    LocalDate getReceivedDate();
    LocalDate getExpiryDate();
    String getPurchaseBillNo();
    LocalDate getPurchaseDate();
    String getManufacturer();
    String getSupplierName();
    BigDecimal getValue();
}
