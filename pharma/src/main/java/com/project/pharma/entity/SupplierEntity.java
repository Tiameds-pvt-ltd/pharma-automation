package com.project.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pharma_supplier")
public class SupplierEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer supplierId;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "supplier_mobile")
    private Long supplierMobile;

    @Column(name = "supplier_email")
    private String supplierEmail;

    @Column(name = "supplier_gstin_no")
    private String supplierGstinNo;

    @Column(name = "supplier_gst_type")
    private String supplierGstType;

    @Column(name = "supplier_address")
    private String supplierAddress;
}
