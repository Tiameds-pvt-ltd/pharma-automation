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
@Table(name="pharma_doctor_details")
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(name = "doctor_initial")
    private String doctorInitial;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "doctor_speciality")
    private String doctorSpeciality;

    @Column(name = "doctor_qualification")
    private String doctorQualification;

    @Column(name = "doctor_mobile")
    private Long doctorMobile;

    @Column(name = "doctor_venue")
    private String doctorVenue;

    @Column(name = "doctor_entered_date")
    private LocalDate doctorEnteredDate;

    @Column(name = "doctor_entered_by")
    private Long doctorEnteredBy;

    @PrePersist
    protected void onCreate() {
        this.doctorEnteredDate = LocalDate.now();
    }
}
