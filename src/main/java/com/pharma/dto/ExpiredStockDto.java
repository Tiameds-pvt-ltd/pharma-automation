package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpiredStockDto {

    private UUID itemId;
    private String itemName;
    private String batchNo;
    private Long packageQuantity;
    private LocalDate expiryDate;
    private UUID supplierId;
    private String supplierName;

}

