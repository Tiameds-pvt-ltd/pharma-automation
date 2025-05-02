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
public class SupplierDto {

    private UUID supplierId;
    private String supplierName;
    private Long supplierMobile;
    private String supplierEmail;
    private String supplierGstinNo;
    private String supplierGstType;
    private String supplierAddress;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;
}
