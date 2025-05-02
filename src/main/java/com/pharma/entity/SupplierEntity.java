package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pharma_supplier")
public class SupplierEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "supplier_id", updatable = false, nullable = false, unique = true)
    private UUID supplierId;

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

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date")
    private LocalDate modifiedDate;

    @PrePersist
    public void generateUUID() {
        if (supplierId == null) {
            supplierId = UUID.randomUUID();
        }
    }
}
