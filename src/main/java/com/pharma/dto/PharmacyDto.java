package com.pharma.dto;

import com.pharma.entity.User;
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
    private String name;
    private String address;
    private String city;
    private String state;
    private String description;
    private Boolean isActive;
    private String gstNo;
    private String licenseNo;
    private String pharmaLogo;
    private String pharmaZip;
    private String pharmaCountry;
    private String pharmaPhone;
    private String pharmaEmail;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

//    private String licenseProof;
//    private String gstProof;

}
