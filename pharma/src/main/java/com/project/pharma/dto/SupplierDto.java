package com.project.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDto {

    private Integer supplierId;
    private String supplierName;
    private Long supplierMobile;
    private String supplierEmail;
    private String supplierGstinNo;
    private String supplierGstType;
    private String supplierAddress;
}
