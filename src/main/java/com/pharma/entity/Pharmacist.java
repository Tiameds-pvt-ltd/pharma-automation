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
@Table(name = "pharma_pharmacist")
public class Pharmacist {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "pharmacist_id", updatable = false, nullable = false, unique = true)
    private UUID pharmacistId;

    @Column(name = "pharmacy_name")
    private String pharmacistName;

    @Column(name = "contact_no")
    private Long contactNo;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "zip_code")
    private Long zipCode;

    @Column(name = "owner_or_not")
    private String ownerOrNot;

    @Column(name = "security_id")
    private String securityId;

    @Column(name = "security_proof")
    private String securityProof;

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
        if (pharmacistId == null) {
            pharmacistId = UUID.randomUUID();
        }

    }

    @ManyToMany(mappedBy = "pharmacists")
    private Set<Pharmacy> pharmacies = new HashSet<>();

}
