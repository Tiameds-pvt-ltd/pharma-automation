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
@Table(name="pharma_patient_details")
public class PatientDetailsEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "patient_id", updatable = false, nullable = false, unique = true)
    private UUID patientId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "patient_no")
    private Long patientNumber;

    @Column(name = "patient_mobile")
    private Long patientMobile;

    @Column(name = "patient_email")
    private String patientEmail;

    @Column(name = "patient_address")
    private String patientAddress;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_Date", insertable = false)
    private LocalDate modifiedDate;

    @PrePersist
    public void generateUUID() {
        if (patientId == null) {
            patientId = UUID.randomUUID();
        }
    }

}
