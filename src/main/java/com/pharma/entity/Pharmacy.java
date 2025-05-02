package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pharma_pharmacy")
public class Pharmacy {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "pharmacy_id", updatable = false, nullable = false, unique = true)
    private UUID pharmacyId;

    @Column(name = "pharmacy_name")
    private String pharmacyName;

    @Column(name = "address")
    private String address;

    @Column(name = "zip_code")
    private Long zipCode;

    @Column(name = "gst_no")
    private String gstNo;

    @Column(name = "license_no")
    private String licenseNo;

    @Column(name = "license_proof")
    private String licenseProof;

    @Column(name = "gst_proof")
    private String gstProof;

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
        if (pharmacyId == null) {
            pharmacyId = UUID.randomUUID();
        }

    }

    @ManyToMany
    @JoinTable(
            name = "pharma_pharmacy_pharmacist",
            joinColumns = @JoinColumn(name = "pharmacy_id"),
            inverseJoinColumns = @JoinColumn(name = "pharmacist_id")
    )
    private Set<Pharmacist> pharmacists = new HashSet<>();
}
