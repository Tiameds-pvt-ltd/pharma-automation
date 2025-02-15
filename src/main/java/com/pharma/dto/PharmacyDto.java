package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyDto {

    private Long pharmacyId;
    private String pharmacyName;
    private Long pharmacistId;
    private String address;
    private Integer zipCode;
    private Long gstNo;
    private Long licenseNo;
    private String licenseProof;
    private String gstProof;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
