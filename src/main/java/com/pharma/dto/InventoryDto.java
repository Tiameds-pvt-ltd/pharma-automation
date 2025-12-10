package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    private UUID invItemId;
    private UUID itemId;
    private Long packageQuantity;
    private Long pharmacyId;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
