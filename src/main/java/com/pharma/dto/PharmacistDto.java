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
public class PharmacistDto {

    private UUID pharmacistId;
    private String pharmacistName;
    private Long contactNo;
    private String email;
    private String address;
    private Long zipCode;
    private String ownerOrNot;
    private String securityId;
    private String securityProof;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

//    private Set<PharmacyDto> pharmacyDtos;

    private Set<UUID> pharmacyIds;


}
