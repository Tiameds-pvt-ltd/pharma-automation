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
public class PharmacistDto {

    private Long pharmacistId;
    private String pharmacistName;
    private Long contactNo;
    private String email;
    private String address;
    private Integer zipCode;
    private Long securityId;
    private String securityProof;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
