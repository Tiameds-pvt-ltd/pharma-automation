package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDto {

    private Long pharmacyId;
    private String name;
    private String address;
    private Long zipCode;
    private String gstNo;
    private String licenseNo;
    private String licenseProof;
    private String gstProof;
    private Boolean isActive;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
