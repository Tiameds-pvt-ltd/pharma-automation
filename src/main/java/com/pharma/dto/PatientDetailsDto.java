package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDetailsDto {

    private Long patientId;
    private String patientName;
    private Long patientNumber;
    private Long patientMobile;
    private String patientAddress;
    private LocalDate patientEnteredDate;
    private Long patientEnteredBy;
}
