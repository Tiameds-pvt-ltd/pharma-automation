package com.pharma.dto;

import java.time.LocalDate;

public interface ItemDayWiseSaleDto {

    LocalDate getBillDateTime();
    String getItemName();
    Long getQuantity();
}
