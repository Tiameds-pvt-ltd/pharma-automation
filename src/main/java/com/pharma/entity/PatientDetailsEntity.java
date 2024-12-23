package com.pharma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pharma_patient_details")
public class PatientDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "patient_no")
    private Long patientNumber;

    @Column(name = "patient_mobile")
    private Integer patientMobile;

    @Column(name = "patient_address")
    private String patientAddress;

    @Column(name = "patient_entered_date")
    private LocalDate patientEnteredDate;

    @Column(name = "patient_entered_by")
    private Long patientEnteredBy;

    @PrePersist
    protected void onCreate() {
        this.patientEnteredDate = LocalDate.now();
    }
}
