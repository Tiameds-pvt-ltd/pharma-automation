package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDetailsDto {

    private UUID patientId;
    private String patientName;
    private Long patientNumber;
    private Long patientMobile;
    private String patientEmail;
    private String patientAddress;
    private Long pharmacyId;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
