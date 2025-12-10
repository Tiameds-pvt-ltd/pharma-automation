package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariantDto {

    private UUID variantId;
    private String variantName;
    private Long pharmacyId;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

    private List<UnitDto> unitDtos = new ArrayList<>();

}
