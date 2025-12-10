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
@Table(name="pharma_doctor_details")
public class DoctorEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "doctor_id", updatable = false, nullable = false, unique = true)
    private UUID doctorId;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "doctor_speciality")
    private String doctorSpeciality;

    @Column(name = "doctor_qualification")
    private String doctorQualification;

    @Column(name = "doctor_mobile")
    private Long doctorMobile;

    @Column(name = "doctor_email")
    private String doctorEmail;

    @Column(name = "doctor_venue")
    private String doctorVenue;

    @Column(name = "doctor_license_number")
    private String doctorLicenseNumber;

    @Column(name = "pharmacy_id")
    private Long pharmacyId;

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
        if (doctorId == null) {
            doctorId = UUID.randomUUID();
        }
    }
}
