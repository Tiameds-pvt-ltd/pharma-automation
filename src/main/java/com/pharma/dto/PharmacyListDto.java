package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyListDto {
    private Long pharmacyId;
    private String name;
    private String address;
    private String city;
    private String state;
    private Boolean isActive;
    private String description;
    private String createdByName;
}
